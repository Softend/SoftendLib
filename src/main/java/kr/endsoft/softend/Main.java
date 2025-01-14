package kr.endsoft.softend;

import kr.endsoft.softend.bstats.Metrics;
import kr.endsoft.softend.config.PluginConfig;
import kr.endsoft.softend.listener.PluginListener;
import kr.endsoft.softend.util.EventUtil;
import kr.endsoft.softend.util.TerminalColorUtil;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter
    private static Main instance;

    @Getter
    private static Metrics metrics;

    @Override
    public void onEnable() {
        instance = this;
        metrics = new Metrics(this, PluginConfig.PLUGIN_ID);

        EventUtil.registerEvents(this, new PluginListener());
        getLogger().info(TerminalColorUtil.formatColor("§5" + PluginConfig.STARTUP_MSG));
    }

    @Override
    public void onDisable() {

    }
}
