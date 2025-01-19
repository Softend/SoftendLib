package kr.endsoft.softend.holder;

import kr.endsoft.softend.Main;
import kr.endsoft.softend.exception.paginate.PaginateException;
import kr.endsoft.softend.util.ItemUtil;
import kr.endsoft.softend.config.PaginateConfig;
import lombok.Getter;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PaginateHolder<T> implements InventoryHolder {

    @Getter
    private final PaginateConfig<T> config;
    @Getter
    private final int currentPage;

    private final Inventory inventory;

    public PaginateHolder(PaginateConfig<T> config, int currentPage) {
        this.config = config;
        this.currentPage = currentPage;

        if (config.inventoryType() != null) {
            this.inventory = Main.getInstance().getServer().createInventory(this, Objects.requireNonNull(config.inventoryType()), config.title());
        } else if (config.size() != null) {
            this.inventory = Main.getInstance().getServer().createInventory(this, Objects.requireNonNull(config.size()), config.title());
        } else {
            throw new PaginateException("인벤토리 종류와 인벤토리 크기 값 중 하나 이상은 null이 아니어야합니다!");
        }

        if (currentPage < 1) {
            throw new PaginateException("현재 페이지는 양수여야 합니다!");
        }

        if (config.itemPerPage() > inventory.getSize()) {
            throw new PaginateException("페이지 당 아이템 개수는 인벤토리의 크기보다 클 수 없습니다!");
        }

        PaginateConfig.ItemList<T> itemList = config.items();
        if (currentPage > itemList.maxPage()) {
            throw new PaginateException("현재 페이지는 최대 페이지보다 작아야 합니다!");
        }

        itemList.getItemsOfPage(currentPage).forEach(item -> {
            ItemStack itemStack = config.generateItem().apply(item);
            inventory.addItem(itemStack);
        });

        PaginateConfig.ButtonData nextBT = config.nextButton();
        PaginateConfig.ButtonData prevBT = config.prevButton();

        inventory.setItem(nextBT.getSlot(), ItemUtil.addData(nextBT.getItemStack(), new NamespacedKey(Main.getInstance(), "paginate_next"), PersistentDataType.BOOLEAN, true));
        inventory.setItem(prevBT.getSlot(), ItemUtil.addData(prevBT.getItemStack(), new NamespacedKey(Main.getInstance(), "paginate_prev"), PersistentDataType.BOOLEAN, true));

        PaginateConfig.ButtonData closeBT = config.closeButton();
        if (closeBT != null) {
            inventory.setItem(closeBT.getSlot(), ItemUtil.addData(closeBT.getItemStack(), new NamespacedKey(Main.getInstance(), "paginate_close"), PersistentDataType.BOOLEAN, true));
        }
    }

    public PaginateHolder<T> nextPage() {
        PaginateConfig.ItemList<T> itemList = config.items();
        if (currentPage >= itemList.maxPage()) {
            return this;
        }

        return new PaginateHolder<>(config, currentPage + 1);
    }

    public PaginateHolder<T> prevPage() {
        if (currentPage <= 1) {
            return this;
        }

        return new PaginateHolder<>(config, currentPage - 1);
    }

    public String getId() {
        return config.id();
    }

    /**
     * Get the object's inventory.
     *
     * @return The inventory.
     */
    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
