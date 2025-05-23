package kr.endsoft.softend;

import kr.endsoft.softend.bstats.Metrics;
import kr.endsoft.softend.command.CommandManager;
import kr.endsoft.softend.command.ReloadCommand;
import kr.endsoft.softend.config.PluginConfig;
import kr.endsoft.softend.file.config.MessageExpansion;
import kr.endsoft.softend.file.config.MessageLoader;
import kr.endsoft.softend.file.config.MessageType;
import kr.endsoft.softend.listener.PluginListener;
import kr.endsoft.softend.util.EventUtil;
import kr.endsoft.softend.util.TerminalColorUtil;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private static Metrics metrics;

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        instance = this;
        metrics = new Metrics(this, PluginConfig.PLUGIN_ID);

        EventUtil.registerEvents(this, new PluginListener());
        getLogger().info(TerminalColorUtil.formatColor("§5" + PluginConfig.STARTUP_MSG));

        MessageLoader.registerDefaultTag("prefix", (plugin) -> MessageLoader.getInstance(plugin).getMessageRaw(MessageType.ETC, "prefix").orElse(""));
        MessageLoader.registerDefaultTag("datetime", (plugin) -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        MessageLoader.registerDefaultTag("time", (plugin) -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")));
        MessageLoader.registerDefaultTag("date", (plugin) -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        MessageLoader.registerDefaultTag("player", ((plugin, audience) -> {
            if (!(audience instanceof Player player)) {
                return "";
            }

            return player.getName();
        }));
        CommandManager.getInstance().registerCommand("softend", new ReloadCommand());

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) { //
            if (new MessageExpansion().register()) {
                getLogger().info("PlaceholderAPI 와 연동에 성공하였습니다.");
            } else {
                getLogger().severe("PlaceholderAPI 와 연동에 실패했습니다.");
            }
        }
    }

    @Override
    public void onDisable() {

    }
}
