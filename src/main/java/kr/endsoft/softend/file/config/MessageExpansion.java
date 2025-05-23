package kr.endsoft.softend.file.config;

import kr.endsoft.softend.Main;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class MessageExpansion extends PlaceholderExpansion {
    /**
     * The placeholder identifier of this expansion. May not contain {@literal %},
     * {@literal {}} or _
     *
     * @return placeholder identifier that is associated with this expansion
     */
    @Override
    public @NotNull String getIdentifier() {
        return "softend";
    }

    /**
     * The author of this expansion
     *
     * @return name of the author for this expansion
     */
    @Override
    public @NotNull String getAuthor() {
        return "E&D Soft";
    }

    /**
     * The version of this expansion
     *
     * @return current version of this expansion
     */
    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player != null && player.isOnline()) {
            Player target = player.getPlayer();
            if (!MessageLoader.audienceMap.containsKey(params)) {
                return null;
            }
            return MessageLoader.audienceMap.get(params).apply(Main.getInstance(), target);
        }

        if (!MessageLoader.tagMap.containsKey(params)) {
            return null;
        }
        return MessageLoader.tagMap.get(params).apply(Main.getInstance());
    }
}
