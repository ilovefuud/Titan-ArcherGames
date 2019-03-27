package rip.titan.archergames.managers;

import org.bukkit.Material;
import rip.titan.archergames.ArcherGamesPlugin;
import rip.titan.archergames.kit.impl.God;
import rip.titan.archergames.shop.ShopEntry;
import rip.titan.archergames.shop.impl.ShopItem;
import rip.titan.archergames.shop.impl.ShopKit;
import rip.titan.archergames.storage.ItemStorage;
import us.lemin.core.utils.item.ItemBuilder;

import java.util.LinkedHashMap;
import java.util.Map;

public class ShopManager {

    private final ArcherGamesPlugin plugin;

    private final Map<String, ShopKit> shopKits = new LinkedHashMap<>();
    private final Map<String, ShopItem> shopItems = new LinkedHashMap<>();


    public ShopManager(ArcherGamesPlugin plugin) {
        this.plugin = plugin;
        registerEntries(
                new ShopKit(plugin.getKitManager().getKitByClass(God.class), 100),
                new ShopItem(new ItemBuilder(Material.GOLDEN_APPLE).name("God Apple").durability(1).build(), 500),
                new ShopItem(Material.DIAMOND_SWORD, 100),
                new ShopItem(Material.DIAMOND_HELMET, 100),
                new ShopItem(Material.DIAMOND_CHESTPLATE, 100),
                new ShopItem(Material.DIAMOND_LEGGINGS, 100),
                new ShopItem(Material.DIAMOND_BOOTS, 100),
                new ShopItem(ItemStorage.getByName("Default Helmet").getItemStack(), 100),
                new ShopItem(ItemStorage.getByName("Default Chestplate").getItemStack(), 100),
                new ShopItem(ItemStorage.getByName("Default Leggings").getItemStack(), 100),
                new ShopItem(ItemStorage.getByName("Default Boots").getItemStack(), 100),
                new ShopItem(ItemStorage.getByName("Default Sword").getItemStack(), 100),
                new ShopItem(ItemStorage.getByName("Strength 2 Potion").getItemStack(), 100),
                new ShopItem(ItemStorage.getByName("Speed 2 Potion").getItemStack(), 100)
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
