package kr.ypshop.softend.util;

import kr.ypshop.softend.Main;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

public class EventUtil {

    public static void registerEvents(Plugin plugin, Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> Main.getInstance().getServer().getPluginManager().registerEvents(listener, plugin));
    }

}
