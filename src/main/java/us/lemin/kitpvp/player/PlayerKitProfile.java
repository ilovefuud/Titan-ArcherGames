package us.lemin.kitpvp.player;

import java.util.UUID;
import lombok.Getter;

public class PlayerKitProfile {
    @Getter
    private final UUID id;
    @Getter
    private final String name;
    @Getter
    private final PlayerStatistics statistics = new PlayerStatistics();
    @Getter
    private PlayerState state = PlayerState.SPAWN;

    public PlayerKitProfile(UUID id, String name) {
        this.id = id;
        this.name = name;
    }
}
