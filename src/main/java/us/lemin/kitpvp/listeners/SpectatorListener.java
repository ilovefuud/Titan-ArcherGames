package us.lemin.kitpvp.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import us.lemin.kitpvp.ArcherGamesPlugin;

public class SpectatorListener implements Listener {

    private final ArcherGamesPlugin plugin;

    public SpectatorListener(ArcherGamesPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpectatorDrop(PlayerDropItemEvent event) {
        if (plugin.getPlayerManager().isSpectating(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpectatorPickup(PlayerPickupItemEvent event) {
        if (plugin.getPlayerManager().isSpectating(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onSpectatorInteract(PlayerInteractEvent event) {
        if (plugin.getPlayerManager().isSpectating(event.getPlayer())) {
            event.setCancelled(true);
        }

        // possibly add spectate item to switch between players, if so, edit this
    }

    @EventHandler
    public void onSpectatorInventoryMove(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() != null
                && event.getCurrentItem() != null
                && event.getCurrentItem().getType() != Material.AIR) {
            if (plugin.getPlayerManager().isSpectating(player)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onSpectatorDamageEntity(EntityDamageByEntityEvent event) {
        if ((!(event.getDamager() instanceof Player) && (!(event.getDamager() instanceof Arrow)
                || !(((Arrow) event.getDamager()).getShooter() instanceof Player)))) {
            return;
        }

        Player damager = event.getDamager() instanceof Player ? (Player) event.getDamager() : (Player) ((Arrow) event.getDamager()).getShooter();

        Player victim = (Player) event.getEntity();

        if (!victim.canSee(damager) || plugin.getPlayerManager().isSpectating(damager)) {
            event.setCancelled(true);
        }
    }
}
