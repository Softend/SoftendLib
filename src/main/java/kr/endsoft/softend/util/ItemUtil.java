package kr.endsoft.softend.util;

import kr.endsoft.softend.object.Pair;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

public class ItemUtil {

    /**
     * 아이템의 PersistentDataContainer 에 값을 저장합니다. ( 반환 값을 사용 )
     * @param item
     * @param key
     * @param type
     * @param data
     * @return
     * @param <T>
     * @param <Z>
     */
    public static<T,Z> ItemStack addData(ItemStack item, NamespacedKey key, PersistentDataType<T, Z> type, Z data) {
        ItemStack itemStack = item.clone();
        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        dataContainer.set(key, type, data);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * 아이템의 PersistentDataContainer 에 값이 저장해있는지 여부를 파악합니다.
     * @param itemStack
     * @param key
     * @return
     */
    public static boolean hasData(ItemStack itemStack, NamespacedKey key) {
        return itemStack.getItemMeta().getPersistentDataContainer().has(key);
    }

    /**
     * 아이템의 PersistentDataContainer 에서 값을 불러옵니다. ( 반환 값을 사용 )
     * @param itemStack
     * @param key
     * @param type
     * @return
     * @param <T>
     * @param <Z>
     */
    public static<T,Z> Optional<Z> getData(ItemStack itemStack, NamespacedKey key, PersistentDataType<T,Z> type) {
        ItemMeta itemMeta = itemStack.getItemMeta();

        PersistentDataContainer dataContainer = itemMeta.getPersistentDataContainer();
        return Optional.ofNullable(dataContainer.get(key, type));
    }

    /**
     * 플레이어의 인벤토리 전체에서 해당 아이템을 보유중인지 확인합니다.
     * @param player
     * @param itemStack
     * @param amount
     * @return
     */
    public static boolean containItem(Player player, ItemStack itemStack, int amount) {
        ItemStack item = itemStack.clone();
        item.setAmount(amount);

        return player.getInventory().contains(item);
    }

    /**
     * 플레이어 인벤토리에 아이템을 지급합니다.
     * @param player
     * @param dropItem 공간이 없으면 아이템을 바닥에 드롭할지 여부
     * @param itemStacks
     * @return
     */
    public static List<ItemStack> addItem(Player player, boolean dropItem, ItemStack... itemStacks) {
        List<ItemStack> result = new ArrayList<>();

        Inventory inventory = player.getInventory();
        Arrays.stream(itemStacks).forEach(itemStack -> {
            int slot = inventory.first(itemStack);
            ItemStack currentItem = inventory.getItem(slot);

            if (slot > -1 && currentItem != null && currentItem.getMaxStackSize() < currentItem.getAmount() + itemStack.getAmount()) {
                ItemStack modifyItem = currentItem.clone();
                modifyItem.setAmount(currentItem.getAmount() + itemStack.getAmount());

                inventory.setItem(slot, modifyItem);
                return;
            }

            slot = inventory.firstEmpty();
            if (slot < 0) {
                result.add(itemStack);

                if (dropItem) {
                    player.getWorld().dropItem(player.getLocation(), itemStack);
                }
                return;
            }

            inventory.setItem(slot, itemStack);
        });
        return result;
    }

    public static Pair<Integer, List<Integer>> getItemCounts(HumanEntity player, ItemStack itemStack) {
        int count = 0;
        List<Integer> slots = new ArrayList<>();

        for (int i = 0; i < player.getInventory().getSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack == null || !stack.isSimilar(itemStack)) {
                continue;
            }

            count += stack.getAmount();
            slots.add(i);
        }

        return new Pair<>(count, slots);
    }

    /**
     * 플레이어 인벤토리에서 아이템을 제거합니다.
     * @param player
     * @param itemStack
     * @return
     */
    public static int removeItem(HumanEntity player, ItemStack itemStack) {
        Pair<Integer, List<Integer>> pair = getItemCounts(player, itemStack);

        int removed = 0;
        for (int integer : pair.getSecond()) {
            if (removed >= pair.getFirst()) {
                break;
            }

            ItemStack stack = player.getInventory().getItem(integer);
            if (stack == null || !stack.isSimilar(itemStack)) {
                continue;
            }

            if (itemStack.getAmount() < removed + stack.getAmount()) {
                stack.setAmount(stack.getAmount() - (itemStack.getAmount() - removed));
                removed += (itemStack.getAmount() - removed);
                player.getInventory().setItem(integer, stack);
                continue;
            }

            removed += stack.getAmount();
            stack.setType(Material.AIR);
            player.getInventory().setItem(integer, stack);
        }

        return itemStack.getAmount() - removed;
    }

}
