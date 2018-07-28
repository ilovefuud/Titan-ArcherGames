package us.lemin.kitpvp.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.timer.Timer;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.player.PlayerKitProfile;
import us.lemin.kitpvp.player.PlayerState;

@RequiredArgsConstructor
public class EntityListener implements Listener {
    private final KitPvPPlugin plugin;

    @EventHandler
    public void onShoot(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) event.getEntity().getShooter();

        if (player.getGameMode() != GameMode.SURVIVAL) {
            return;
        }

        PlayerKitProfile profile = plugin.getPlayerManager().getProfile(player);

        if (profile.getState() != PlayerState.FFA) {
            return;
        }

        if (event.getEntity() instanceof EnderPearl) {
            Timer timer = profile.getPearlTimer();

            timer.isActive(); // check active
        }
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();

        if (victim.getAllowFlight()) {
            victim.setFlying(false);
            victim.setAllowFlight(false);
        }

        PlayerKitProfile victimProfile = plugin.getPlayerManager().getProfile(victim);

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            victim.teleport(plugin.getSpawnLocation());
        } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL && !victimProfile.isFallDamageEnabled()) {
            event.setCancelled(true);
            return;
        }

        if (victimProfile.getState() == PlayerState.SPAWN) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player damager = (Player) event.getDamager();
        PlayerKitProfile damagerProfile = plugin.getPlayerManager().getProfile(damager);

        Player victim = (Player) event.getEntity();
        PlayerKitProfile victimProfile = plugin.getPlayerManager().getProfile(victim);

        if (damagerProfile.getState() == PlayerState.SPAWN && victimProfile.getState() == PlayerState.FFA) {
            damagerProfile.setState(PlayerState.FFA);
            damager.sendMessage(CC.RED + "You no longer have spawn protection!");
        }

        victimProfile.getDamageData().put(event.getDamager().getUniqueId(), event.getFinalDamage());
    }
}
