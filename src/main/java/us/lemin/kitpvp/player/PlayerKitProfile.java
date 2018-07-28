package us.lemin.kitpvp.player;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import us.lemin.core.CorePlugin;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.storage.database.MongoRequest;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.kit.Kit;

public class PlayerKitProfile {
    @Getter
    private final UUID id;
    @Getter
    private final String name;
    @Getter
    private final PlayerDamageData damageData = new PlayerDamageData();
    @Getter
    private final PlayerStatistics statistics = new PlayerStatistics();
    @Getter
    private PlayerState state = PlayerState.SPAWN;
    @Getter
    @Setter
    private Kit kit;

    public PlayerKitProfile(UUID id, String name) {
        this.id = id;
        this.name = name;

        CorePlugin.getInstance().getMongoStorage().getOrCreateDocument("kitpvp", id, (document, found) -> {
            if (found) {
                statistics.setDeaths(document.getInteger("deaths", 0));
                statistics.setEventWins(document.getInteger("event_wins", 0));
                statistics.setHighestKillStreak(document.getInteger("highest_kill_streak", 0));
                statistics.setKills(document.getInteger("kills", 0));
                statistics.setKillStreak(document.getInteger("kill_streak", 0));
                statistics.setPesos(document.getInteger("pesos", 0));
            }
        });
    }

    public void save(KitPvPPlugin plugin) {
        save(true, plugin);
    }

    public void save(boolean async, KitPvPPlugin plugin) {
        Runnable runnable = () -> {
            MongoRequest request = MongoRequest.newRequest("kitpvp", id)
                    .put("deaths", statistics.getDeaths())
                    .put("event_wins", statistics.getEventWins())
                    .put("highest_kill_streak", statistics.getHighestKillStreak())
                    .put("kills", statistics.getKills())
                    .put("kill_streak", statistics.getKillStreak())
                    .put("pesos", statistics.getPesos());

            request.run();
        };

        if (async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }

    public int worth() {
        CoreProfile profile = CorePlugin.getInstance().getProfileManager().getProfile(id);
        return profile.isDonor() ? 15 : 10;
    }
}
