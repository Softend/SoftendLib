package kr.ypshop.softend.holder;

import kr.ypshop.softend.config.PaginateConfig;
import lombok.Getter;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PaginateHolder<T> implements InventoryHolder {

    @Getter
    private final List<T> items;

    public PaginateHolder(PaginateConfig<T> config) {
        this.items = config.items();
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
