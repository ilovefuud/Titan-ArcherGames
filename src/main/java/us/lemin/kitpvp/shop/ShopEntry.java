package us.lemin.kitpvp.shop;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.player.PlayerProfile;
import us.lemin.kitpvp.server.ServerStage;

public abstract class ShopEntry {

    protected ArcherGamesPlugin plugin;
    protected int cost;


    public ShopEntry(ArcherGamesPlugin plugin, ItemStack icon, int cost) {
        this.plugin = plugin;
        this.cost = cost;
    }

    public ShopEntry(ArcherGamesPlugin plugin, Material icon, int cost){
        this(plugin, new ItemStack(icon), cost);
    }

    protected abstract void purchase(Player player);

    protected boolean canPurchase(Player player) {
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
