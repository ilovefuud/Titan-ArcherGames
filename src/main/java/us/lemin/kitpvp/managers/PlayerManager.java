package us.lemin.kitpvp.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import us.lemin.core.CorePlugin;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.player.PlayerKitProfile;
import us.lemin.kitpvp.player.PlayerState;

@RequiredArgsConstructor
public class PlayerManager {
    private final KitPvPPlugin plugin;
    private final Map<UUID, PlayerKitProfile> profiles = new HashMap<>();

    public void createProfile(UUID id, String name) {
        PlayerKitProfile profile = new PlayerKitProfile(id, name);
        profiles.put(id, profile);
    }

    public PlayerKitProfile getProfile(Player player) {
        return profiles.get(player.getUniqueId());
    }

    public void removeProfile(Player player) {
        profiles.remove(player.getUniqueId());
    }

    public void saveAllProfiles() {
        for (PlayerKitProfile profile : profiles.values()) {
            profile.save(false, plugin);
        }
    }

    public void loseSpawnProtection(Player player) {
        PlayerKitProfile profile = getProfile(player);

        profile.setState(PlayerState.FFA);

        CoreProfile coreProfile = CorePlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (coreProfile.hasDonor()) {
            player.setFlying(false);
            player.setAllowFlight(false);

            profile.setFallDamageEnabled(false);

            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                if (!player.isOnline()) {
                    return;
                }

                profile.setFallDamageEnabled(true);
            }, 20L * 5);
        }

        player.sendMessage(CC.RED + "You no longer have spawn protection!");
    }

    public void acquireSpawnProtection(Player player) {
        PlayerKitProfile profile = getProfile(player);

        profile.setState(PlayerState.SPAWN);
        profile.setFallDamageEnabled(true);

        CoreProfile coreProfile = CorePlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());

        if (coreProfile.hasDonor()) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

        player.sendMessage(CC.GREEN + "You have acquired spawn protection.");
    }
}
