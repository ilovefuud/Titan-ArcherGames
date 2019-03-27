package rip.titan.archergames.storage;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.item.ItemBuilder;

public enum ItemStorage {
    DEFAULT_HELMET("Default Helmet", new ItemBuilder(Material.DIAMOND_HELMET)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
            .enchant(Enchantment.PROTECTION_EXPLOSIONS, 4)
            .enchant(Enchantment.PROTECTION_PROJECTILE, 4).build()),
    DEFAULT_CHESTPLATE("Default Chestplate", new ItemBuilder(Material.DIAMOND_CHESTPLATE)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
            .enchant(Enchantment.PROTECTION_EXPLOSIONS, 4)
            .enchant(Enchantment.PROTECTION_PROJECTILE, 4).build()),
    DEFAULT_LEGGINGS("Default Leggings", new ItemBuilder(Material.DIAMOND_LEGGINGS)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
            .enchant(Enchantment.PROTECTION_EXPLOSIONS, 4)
            .enchant(Enchantment.PROTECTION_PROJECTILE, 4).build()),
    DEFAULT_BOOTS("Default Boots", new ItemBuilder(Material.DIAMOND_BOOTS)
            .enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 4)
            .enchant(Enchantment.PROTECTION_EXPLOSIONS, 4)
            .enchant(Enchantment.PROTECTION_PROJECTILE, 4).build()),
    DEFAULT_SWORD("Default Sword", new ItemBuilder(Material.DIAMOND_SWORD)
            .enchant(Enchantment.DAMAGE_ALL, 5)
            .enchant(Enchantment.DURABILITY, 3)
            .enchant(Enchantment.FIRE_ASPECT, 2).build()),
    STRENGTH_2("Strength 2 Potion", new ItemBuilder(Material.POTION).durability(8233).build()),
    SPEED_2("Speed 2 Potion", new ItemBuilder(Material.POTION).durability(8226).build());

    private String name;
    private ItemStack itemStack;

    ItemStorage(String name, ItemStack itemStack) {
        this.name = name;
        this.itemStack = itemStack;
    }

    public static ItemStorage getByName(String name) {
        for (ItemStorage itemStorage : values()) {
            if (itemStorage.name().equalsIgnoreCase(name)) {
                return itemStorage;
            }
        }
        return null;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }


}
