package us.lemin.kitpvp.shop.impl;

import org.bukkit.entity.Player;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.kit.Kit;
import us.lemin.kitpvp.player.PlayerProfile;
import us.lemin.kitpvp.shop.ShopEntry;

public class ShopKit extends ShopEntry {

    private String name;
    private Kit kit;

    public ShopKit(ArcherGamesPlugin plugin, Kit kit, int cost) {
        super(plugin, kit.getIcon(), cost);
        this.kit = kit;
        this.name = kit.getName();

    }

    @Override
    public void purchase(Player player) {
        if (canPurchase(player)) {
            PlayerProfile profile = plugin.getPlayerManager().getProfile(player);
            // check if they have enough credits, if not return and send
            // message saying they dont have enough credits
            // automatically equip or throw in inventory?
            kit.apply(player);
        }
    }

    public Kit getKit() {
        return kit;
    }

    public String getName() {
        return name;
    }
}
