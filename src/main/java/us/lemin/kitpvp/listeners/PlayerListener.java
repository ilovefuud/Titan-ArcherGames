package us.lemin.kitpvp.listeners;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.player.PlayerKitProfile;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final KitPvPPlugin plugin;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            plugin.getProfileManager().createProfile(event.getUniqueId(), event.getName());
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        UUID id = player.getUniqueId();
        PlayerKitProfile profile = plugin.getProfileManager().getProfile(id);

        if (profile == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, CC.RED + "Your data failed to load for KitPvP. Try logging in again.");
            return;
        } else if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            plugin.getProfileManager().removeProfile(id);
            return;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        player.sendMessage(CC.SEPARATOR);
        player.sendMessage(CC.PRIMARY + "Welcome to " + CC.SECONDARY + "Lemin KitPvP" + CC.PRIMARY + "!");
        player.sendMessage(CC.SEPARATOR);
    }
}
