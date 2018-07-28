package us.lemin.kitpvp.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.player.PlayerKitProfile;
import us.lemin.kitpvp.player.PlayerState;

@RequiredArgsConstructor
public class EntityListener implements Listener {
    private final KitPvPPlugin plugin;

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID) {
            victim.teleport(plugin.getSpawnLocation());
        }

        PlayerKitProfile victimProfile = plugin.getProfileManager().getProfile(victim);

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
        PlayerKitProfile damagerProfile = plugin.getProfileManager().getProfile(damager);

        Player victim = (Player) event.getEntity();
        PlayerKitProfile victimProfile = plugin.getProfileManager().getProfile(victim);

        if (damagerProfile.getState() == PlayerState.SPAWN && victimProfile.getState() == PlayerState.FIGHTING) {
            damagerProfile.setState(PlayerState.FIGHTING);
            damager.sendMessage(CC.RED + "You no longer have spawn protection!");
        }

        victimProfile.getDamageData().put(event.getDamager().getUniqueId(), event.getFinalDamage());
    }
}
