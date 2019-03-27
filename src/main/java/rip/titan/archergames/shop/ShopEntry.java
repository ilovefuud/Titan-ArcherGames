package rip.titan.archergames.shop;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import rip.titan.archergames.ArcherGamesPlugin;
import rip.titan.archergames.player.PlayerProfile;
import rip.titan.archergames.server.ServerStage;
import us.lemin.core.utils.message.CC;

public abstract class ShopEntry {

    @Getter
    private int cost;
    @Getter
    private ItemStack icon;



    public ShopEntry(ItemStack icon, int cost) {
        this.cost = cost;
        this.icon = icon;
    }

    public ShopEntry(Material icon, int cost){
        this(new ItemStack(icon), cost);
    }

    protected abstract void purchase(Player player);

    protected boolean canPurchase(Player player) {
        ArcherGamesPlugin plugin = ArcherGamesPlugin.getInstance();
        ServerStage serverStage = plugin.getServerManager().getServerStage();
        if (serverStage != ServerStage.GRACE) {
            player.sendMessage(CC.RED + "You can only use the shop during Grace period.");
            return false;
        }
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player);
        if (profile.getCredits() >= cost) {
            player.sendMessage(CC.RED + "nigga u broke as hell");
            return false;
        }
        return true;
    }

    public int getCost() {
        return cost;
    }
}
