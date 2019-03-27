package rip.titan.archergames.listeners;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import rip.titan.archergames.ArcherGamesPlugin;
import rip.titan.archergames.player.PlayerProfile;
import rip.titan.archergames.server.ServerStage;
import us.lemin.core.utils.message.CC;

public class EntityListener implements Listener {

    public EntityListener(ArcherGamesPlugin plugin) {
        this.plugin = plugin;
    }

    private final ArcherGamesPlugin plugin;


    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Arrow && event.getEntity().getShooter() instanceof Player) {
            PlayerProfile profile = plugin.getPlayerManager().getProfile(((Player) event.getEntity().getShooter()).getPlayer());
            if (profile.hasGodKit()) {
                Location location = event.getEntity().getLocation();
                location.getWorld().createExplosion(location, 2);
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (plugin.getServerManager().getServerStage() != ServerStage.FIGHTING) {
            event.setCancelled(true);
        }
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = ((Player) event.getEntity()).getPlayer();
        boolean died = event.getFinalDamage() > player.getHealth();
        if (died) {
            plugin.getPlayerManager().removePlayer(player);

        }
    }


    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();


        PlayerProfile profile = plugin.getPlayerManager().getProfile(player);

        if (plugin.getServerManager().getServerStage() != ServerStage.FIGHTING) {
            event.setCancelled(true);
            player.sendMessage(CC.RED + "Please wait until the Grace period is over.");
        }
    }


    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)
                || (!(event.getDamager() instanceof Player) && (!(event.getDamager() instanceof Arrow)
                || !(((Arrow) event.getDamager()).getShooter() instanceof Player)))) {
            return;
        }

        Player damager = event.getDamager() instanceof Player ? (Player) event.getDamager() : (Player) ((Arrow) event.getDamager()).getShooter();

        Player victim = (Player) event.getEntity();

        PlayerProfile victimProfile = plugin.getPlayerManager().getProfile(victim);

        victimProfile.setLastAttacker(damager.getUniqueId());
        boolean died = event.getFinalDamage() >= victim.getHealth();
        if (died) {
            for (ItemStack armorContent : victim.getInventory().getArmorContents()) {
                victim.getWorld().dropItemNaturally(victim.getLocation(), armorContent);
            }
            for (ItemStack inventoryContent : victim.getInventory().getContents()) {
                victim.getWorld().dropItemNaturally(victim.getLocation(), inventoryContent);
            }
            plugin.getPlayerManager().removePlayer(victim);
        }
    }
}
