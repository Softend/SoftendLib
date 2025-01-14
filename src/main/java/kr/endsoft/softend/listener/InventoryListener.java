package kr.endsoft.softend.listener;

import kr.endsoft.softend.util.ItemUtil;
import kr.endsoft.softend.Main;
import kr.endsoft.softend.holder.PaginateHolder;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        Inventory inventory = event.getClickedInventory();
        if (inventory == null) {
            return;
        }

        if (!(inventory.getHolder() instanceof PaginateHolder<?> paginate)) {
            return;
        }
        event.setCancelled(true);

        ItemStack itemStack = inventory.getItem(event.getSlot());
        if (itemStack == null) {
            return;
        }

        if (ItemUtil.hasData(itemStack, new NamespacedKey(Main.getInstance(), "paginate_next"))) {
            PaginateHolder<?> holder = paginate.nextPage();
            event.getWhoClicked().openInventory(holder.getInventory());
        } else if (ItemUtil.hasData(itemStack, new NamespacedKey(Main.getInstance(), "paginate_prev"))) {
            PaginateHolder<?> holder = paginate.prevPage();
            event.getWhoClicked().openInventory(holder.getInventory());
        } else if (ItemUtil.hasData(itemStack, new NamespacedKey(Main.getInstance(), "paginate_close"))) {
            event.getWhoClicked().closeInventory();
        }
    }

}
