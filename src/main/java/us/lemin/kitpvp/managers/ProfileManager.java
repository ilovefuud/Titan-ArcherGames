package us.lemin.kitpvp.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.player.PlayerKitProfile;

@RequiredArgsConstructor
public class ProfileManager {
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
}
