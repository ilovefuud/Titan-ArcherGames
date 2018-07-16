package us.lemin.kitpvp.managers;

import lombok.RequiredArgsConstructor;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.player.PlayerKitProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class ProfileManager {
    private final KitPvPPlugin plugin;
    private final Map<UUID, PlayerKitProfile> profiles = new HashMap<>();

    public void createProfile(UUID id, String name) {
        PlayerKitProfile profile = new PlayerKitProfile(id, name);
        profiles.put(id, profile);
    }

    public PlayerKitProfile getProfile(UUID id) {
        return profiles.get(id);
    }

    public void removeProfile(UUID id) {
        profiles.remove(id);
    }
}
