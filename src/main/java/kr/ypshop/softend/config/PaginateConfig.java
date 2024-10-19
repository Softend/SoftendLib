package kr.ypshop.softend.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public interface PaginateConfig<T> {

    String title();

    ButtonData nextButton();
    ButtonData prevButton();

    Function<T, ItemStack> generateItem();
    ItemList<T> items();

    int itemPerPage();
    int currentPage();


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

        public int maxPage() {
            return Math.max(1,items.size() / itemPerPage);
        }

        public List<T> getItemsOfPage(int page) {
            List<T> result = new ArrayList<>();
            for (int i = (page - 1) * itemPerPage; i < Math.min(items.size(), page * itemPerPage); i++) {
                result.add(items.get(i));
            }

            return result;
        }
    }

}
