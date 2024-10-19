package kr.ypshop.softend.builder;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Getter
public class ItemBuilder {

    private final ItemStack itemStack;
    private final ItemMeta itemMeta;

    public ItemBuilder(Material material) {
        itemStack = new ItemStack(material);
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        itemMeta = itemStack.getItemMeta();
    }

    public ItemBuilder displayName(Component component) {
        itemMeta.displayName(component);
        return this;
    }

    public Component displayName() {
        return itemMeta.displayName();
    }

    public ItemBuilder displayName(String string) {
        itemMeta.displayName(Component.text(string));
        return this;
    }

    public ItemBuilder addLore(Component... component) {
        List<Component> components = itemMeta.lore();
        if (components == null) {
            components = new ArrayList<>();
        }

        components.addAll(Arrays.asList(component));
        return this;
    }

    public ItemBuilder addLore(String... string) {
        List<Component> components = itemMeta.lore();
        if (components == null) {
            components = new ArrayList<>();
        }

        components.addAll(Arrays.stream(string).map(Component::text).toList());
        return this;
    }

    public List<Component> lore() {
        return Objects.requireNonNullElse(itemMeta.lore(), new ArrayList<>());
    }

    public <T,Z> ItemBuilder addPersistentData(NamespacedKey key, PersistentDataType<T,Z> type, Z value) {
        itemMeta.getPersistentDataContainer().set(key, type, value);
        return this;
    }

    public <T,Z> Z getPersistentData(NamespacedKey key, PersistentDataType<T,Z> type) {
        return itemMeta.getPersistentDataContainer().get(key, type);
    }

    public PersistentDataContainer getPersistentContainer() {
        return itemMeta.getPersistentDataContainer();
    }

    public boolean contain(NamespacedKey key) {
        return itemMeta.getPersistentDataContainer().has(key);
    }

    public ItemBuilder removePersistentData(NamespacedKey key) {
        itemMeta.getPersistentDataContainer().remove(key);
        return this;
    }

    public ItemBuilder amount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public int amount() {
        return itemStack.getAmount();
    }

    public ItemStack build() {
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

}
