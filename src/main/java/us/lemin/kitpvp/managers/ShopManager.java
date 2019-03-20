package us.lemin.kitpvp.managers;

import org.bukkit.Material;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.kit.impl.God;
import us.lemin.kitpvp.shop.ShopEntry;
import us.lemin.kitpvp.shop.impl.ShopItem;
import us.lemin.kitpvp.shop.impl.ShopKit;
import us.lemin.kitpvp.storage.ItemStorage;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShopManager {

    private final ArcherGamesPlugin plugin;

    private final Map<String, ShopKit> shopKits = new LinkedHashMap<>();
    private final Map<String, ShopItem> shopItems = new LinkedHashMap<>();


    public ShopManager(ArcherGamesPlugin plugin) {
        this.plugin = plugin;
        registerEntries(
                new ShopKit(plugin, plugin.getKitManager().getKitByClass(God.class), 100),
                new ShopItem(plugin, new ItemBuilder(Material.GOLDEN_APPLE).name("God Apple").durability(1).build(), 500),
                new ShopItem(plugin, Material.DIAMOND_SWORD, 100),
                new ShopItem(plugin, Material.DIAMOND_HELMET, 100),
                new ShopItem(plugin, Material.DIAMOND_CHESTPLATE, 100),
                new ShopItem(plugin, Material.DIAMOND_LEGGINGS, 100),
                new ShopItem(plugin, Material.DIAMOND_BOOTS, 100),
                new ShopItem(plugin, ItemStorage.getByName("Default Helmet").getItemStack(), 100),
                new ShopItem(plugin, ItemStorage.getByName("Default Chestplate").getItemStack(), 100),
                new ShopItem(plugin, ItemStorage.getByName("Default Leggings").getItemStack(), 100),
                new ShopItem(plugin, ItemStorage.getByName("Default Boots").getItemStack(), 100),
                new ShopItem(plugin, ItemStorage.getByName("Default Sword").getItemStack(), 100),
                new ShopItem(plugin, ItemStorage.getByName("Strength 2 Potion").getItemStack(), 100),
                new ShopItem(plugin, ItemStorage.getByName("Speed 2 Potion").getItemStack(), 100)
        );
    }


    private void registerEntries(ShopEntry... shopEntries) {
        for (ShopEntry shopEntry : shopEntries) {
            if (shopEntry.getClass().isAssignableFrom(ShopItem.class)) {
                ShopItem shopItem = (ShopItem) shopEntry;
                shopItems.put(shopItem.getName(), shopItem);
            }
            if (shopEntry.getClass().isAssignableFrom(ShopKit.class)) {
                ShopKit shopKit = (ShopKit) shopEntry;
                shopKits.put(shopKit.getName(), shopKit);
            }
        }
    }

    public Map<String, ShopItem> getShopItems() {
        return shopItems;
    }

    public Map<String, ShopKit> getShopKits() {
        return shopKits;
    }

    public ShopItem getShopItemByName(String string) {
        return shopItems.get(string);
    }

    public ShopKit getShopKitByName(String string) {
        return shopKits.get(string);
    }
}
