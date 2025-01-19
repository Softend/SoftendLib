package kr.endsoft.softend.file.config;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.intellij.lang.annotations.Subst;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Getter
public class MessageLoader {

    private static final Map<Plugin, MessageLoader> instanceMap = new HashMap<>();
    public static MessageLoader getInstance(Plugin plugin) {
        if (!instanceMap.containsKey(plugin)) {
            instanceMap.put(plugin, new MessageLoader(plugin));
        }

        return instanceMap.get(plugin);
    }

    private final Plugin plugin;
    private final Map<String, String> contentMap = new HashMap<>();
    private static final Map<String, Function<Plugin, String>> tagMap = new HashMap<>();
    private static final Map<String, BiFunction<Plugin,Audience,String>> audienceMap = new HashMap<>();

    private MessageLoader(Plugin plugin) {
        this.plugin = plugin;
        loadContent();
    }

    public static void registerDefaultTag(String name, Function<Plugin, String> consumer) {
        unregisterDefaultTagAudience(name);
        tagMap.put(name, consumer);
    }
    public static void registerDefaultTag(String name, BiFunction<Plugin,Audience, String> consumer) {
        unregisterDefaultTag(name);
        audienceMap.put(name, consumer);
    }

    public static void unregisterDefaultTagAudience(String name) {
        audienceMap.remove(name);
    }
    public static void unregisterDefaultTag(String name) {
        tagMap.remove(name);
    }

    public boolean loadContent() {
        plugin.saveResource("messages.yml", false);

        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.getLogger().severe("messages.yml 이 존재하지 않아 메시지 데이터를 불러오는데 실패했습니다.");
            return false;
        }
        contentMap.clear();

        FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
        configuration.getValues(true).forEach((key, obj) -> {
            if (!(obj instanceof String value)) {
                return;
            }
            contentMap.put(key, value);
        });
        return true;
    }

    public Optional<String> getMessageRaw(MessageType type, String key) {
        return Optional.ofNullable(contentMap.get(type.getKey() + "." + key));
    }

    public Optional<Component> getMessage(MessageType type, String key, Audience audience, boolean tagResolve) {
        Optional<String> msg = getMessageRaw(type, key);

        if (tagResolve) {
            List<TagResolver> resolvers = new ArrayList<>();
            tagMap.forEach((holderKey, supplier) -> {
                String value = supplier.apply(plugin);
                resolvers.add(Placeholder.parsed(holderKey, value));
            });
            if (audience != null) {
                audienceMap.forEach((holderKey, supplier) -> {
                    String value = supplier.apply(plugin, audience);
                    resolvers.add(Placeholder.parsed(holderKey, value));
                });
            }

            return msg.map(string -> MiniMessage.miniMessage().deserialize(string, resolvers.toArray(new TagResolver[0])));
        }

        return msg.map(string -> MiniMessage.miniMessage().deserialize(string));
    }

    public Optional<Component> getMessage(MessageType type, String key, Audience audience) {
        return getMessage(type, key, audience, true);
    }

    public Optional<Component> getMessage(MessageType type, String key, Audience audience, boolean defaultTagResolve, TagResolver... placeholder) {
        Optional<String> msg = getMessageRaw(type, key);
        List<TagResolver> resolvers = new ArrayList<>(Arrays.asList(placeholder));
        if (defaultTagResolve) {
            tagMap.forEach((tagKey, supplier) -> {
                String value = supplier.apply(plugin);
                resolvers.add(Placeholder.parsed(tagKey, value));
            });
            if (audience != null) {
                audienceMap.forEach((holderKey, supplier) -> {
                    String value = supplier.apply(plugin, audience);
                    resolvers.add(Placeholder.parsed(holderKey, value));
                });
            }
        }
        return msg.map(string -> MiniMessage.miniMessage().deserialize(string, resolvers.toArray(resolvers.toArray(new TagResolver[0]))));
    }

    public Optional<Component> getMessage(MessageType type, String key, Audience audience, TagResolver... placeholder) {
        return getMessage(type,key,audience,true,placeholder);
    }
}
