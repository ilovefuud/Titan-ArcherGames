package us.lemin.kitpvp.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.player.PlayerKitProfile;

@RequiredArgsConstructor
public class EntityListener implements Listener {
    private final KitPvPPlugin plugin;

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) {
            return;
        }

        Player victim = (Player) event.getEntity();
        PlayerKitProfile victimProfile = plugin.getProfileManager().getProfile(victim);

        victimProfile.getDamageData().put(event.getDamager().getUniqueId(), event.getFinalDamage());
    }
}
