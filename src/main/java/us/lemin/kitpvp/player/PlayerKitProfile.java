package us.lemin.kitpvp.player;

import java.util.UUID;

public class PlayerKitProfile {
    private final UUID id;
    private final String name;

    public PlayerKitProfile(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
