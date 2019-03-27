package rip.titan.archergames.shop.impl;

import lombok.Getter;
import org.bukkit.entity.Player;
import rip.titan.archergames.ArcherGamesPlugin;
import rip.titan.archergames.kit.Kit;
import rip.titan.archergames.player.PlayerProfile;
import rip.titan.archergames.shop.ShopEntry;

public class ShopKit extends ShopEntry {
    @Getter
    private String name;
    @Getter
    private Kit kit;

    public ShopKit(Kit kit, int cost) {
        super(kit.getIcon(), cost);
        this.kit = kit;
        this.name = kit.getName();

    }

    @Override
    public void purchase(Player player) {
        if (canPurchase(player)) {
            ArcherGamesPlugin plugin = ArcherGamesPlugin.getInstance();
            PlayerProfile profile = plugin.getPlayerManager().getProfile(player);
            // check if they have enough credits, if not return and send
            // message saying they dont have enough credits
            // automatically equip or throw in inventory?
            kit.equip(player);
        }
    }

}
