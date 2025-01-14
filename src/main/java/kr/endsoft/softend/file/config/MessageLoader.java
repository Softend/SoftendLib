package kr.endsoft.softend.file.config;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    private MessageLoader(Plugin plugin) {
        this.plugin = plugin;
        loadContent();
    }

    public boolean loadContent() {
        plugin.saveResource("messages.yml", false);

        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.getLogger().severe("message.yml 이 존재하지 않아 메시지 데이터를 불러오는데 실패했습니다.");
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

    public Optional<Component> getMessage(MessageType type, String key) {
        Optional<String> msg = getMessageRaw(type, key);
        return msg.map(string -> MiniMessage.miniMessage().deserialize(string));
    }
}
