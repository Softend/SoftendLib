package kr.endsoft.softend.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public interface PaginateConfig<T> {

    String id();
    Component title();

    ButtonData nextButton();
    ButtonData prevButton();

    @Nullable
    ButtonData closeButton();

    Function<T, ItemStack> generateItem();
    ItemList<T> items();

    int itemPerPage();

    @Nullable
    InventoryType inventoryType();

    @Nullable
    Integer size();


    @Getter
    @AllArgsConstructor
    class ButtonData {
        private ItemStack itemStack;
        private int slot;
    }

    class ItemList<T> {
        private List<T> items = new ArrayList<>();
        private int itemPerPage;

        @SafeVarargs
        public ItemList(int itemPerPage, T... items) {
            this.items.addAll(Arrays.asList(items));
            this.itemPerPage = itemPerPage;
        }

        public ItemList(int itemPerPage, List<T> items) {
            this.items = List.copyOf(items);
            this.itemPerPage = itemPerPage;
        }


        /**
         * 해당 아이템 목록이 가진 페이지의 최대 개수를 반환합니다.
         * 2를 반환 시, ItemList#getItemsOfPage(2) 까지 가능.
         * @return 최대 페이지 값
         */
        public int maxPage() {
            return Math.max(1,items.size() / itemPerPage);
        }

        /**
         * 1페이지부터 시작하며, 해당 페이지에 들어가야 하는 값을 반환합니다.
         * @param page 페이지
         * @return 아이템 목록
         */
        public List<T> getItemsOfPage(int page) {
            List<T> result = new ArrayList<>();
            for (int i = (page - 1) * itemPerPage; i < Math.min(items.size(), page * itemPerPage); i++) {
                result.add(items.get(i));
            }

            return result;
        }
    }

}
