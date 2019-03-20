package us.lemin.kitpvp.shop.impl;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.shop.ShopEntry;

public class ShopItem extends ShopEntry {
    private String name;
    private ItemStack itemStack;

    public ShopItem(ArcherGamesPlugin plugin, ItemStack icon, int cost) {
        super(plugin, icon, cost);
        this.name = icon.getItemMeta().getDisplayName();
    }

    public ShopItem(ArcherGamesPlugin plugin, Material material, int cost) {
        this(plugin, new ItemStack(material), cost);
    }


    @Override
    public void purchase(Player player) {
        if (canPurchase(player)) {
            if (player.getInventory().firstEmpty() != -1) {
                player.getInventory().setItem(player.getInventory().firstEmpty(), itemStack);
            }
        }
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public String getName() {
        return name;
    }
}
