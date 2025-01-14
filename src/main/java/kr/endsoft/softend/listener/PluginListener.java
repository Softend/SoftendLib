package kr.endsoft.softend.listener;

import kr.endsoft.softend.service.plugin.SoftendAddon;
import kr.endsoft.softend.service.ServiceManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;

public class PluginListener implements Listener {

    @EventHandler
    public void onPluginLoad(PluginEnableEvent event) {
        Plugin plugin = event.getPlugin();
        if (!(plugin instanceof SoftendAddon addon)) {
            return;
        }

        addon.getServices().forEach(serviceInstance -> ServiceManager.getInstance().register(serviceInstance, plugin));
    }

    @EventHandler
    public void onPluginUnload(PluginDisableEvent event) {
        ServiceManager.getInstance().unregisterAll(event.getPlugin());
    }

}
