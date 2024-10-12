package kr.ypshop.template;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Getter
    private static final int plugin_id = 0000; //TODO: Bstats ID
    
    @Getter
    private static Main instance;
    @Getter
    private static Metrics metrics;

    @Override
    public void onEnable() {
        instance = this;
        metrics = new Metrics(this, plugin_id);
    }

    @Override
    public void onDisable() {

    }
}
