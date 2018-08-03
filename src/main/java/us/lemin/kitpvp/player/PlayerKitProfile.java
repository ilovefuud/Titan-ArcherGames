package us.lemin.kitpvp.player;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.Setter;
import us.lemin.core.CorePlugin;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.storage.database.MongoRequest;
import us.lemin.core.utils.timer.Timer;
import us.lemin.core.utils.timer.impl.DoubleTimer;
import us.lemin.core.utils.timer.impl.IntegerTimer;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.events.Event;
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
    private final Timer pearlTimer = new DoubleTimer(16);
    @Getter
    private final Timer eventHostTimer = new IntegerTimer(TimeUnit.MINUTES, 5);
    @Getter
    private final int worth;
    @Getter
    @Setter
    private PlayerState state = PlayerState.SPAWN;
    @Getter
    @Setter
    private Kit currentKit;
    @Getter
    private Kit lastKit;
    @Getter
    @Setter
    private Event activeEvent;
    @Getter
    @Setter
    private boolean fallDamageEnabled = true;
    @Getter
    @Setter
    private boolean awaitingTeleport;

    public PlayerKitProfile(KitPvPPlugin plugin, UUID id, String name) {
        this.id = id;
        this.name = name;
        CoreProfile coreProfile = CorePlugin.getInstance().getProfileManager().getProfile(id);
        this.worth = coreProfile.isDonor() ? 15 : 10;

        CorePlugin.getInstance().getMongoStorage().getOrCreateDocument("kitpvp", id, (document, found) -> {
            if (found) {
                String lastKitName = document.getString("last_kit_name");

                if (lastKitName != null) {
                    this.lastKit = plugin.getKitManager().getKitByName(lastKitName);
                }

                statistics.setDeaths(document.getInteger("deaths", 0));
                statistics.setEventWins(document.getInteger("event_wins", 0));
                statistics.setHighestKillStreak(document.getInteger("highest_kill_streak", 0));
                statistics.setKills(document.getInteger("kills", 0));
                statistics.setKillStreak(document.getInteger("kill_streak", 0));
                statistics.setCredits(document.getInteger("credits", 0));
            }
        });
    }

    public void save(KitPvPPlugin plugin) {
        save(true, plugin);
    }

    public void save(boolean async, KitPvPPlugin plugin) {
        Runnable runnable = () -> {
            MongoRequest request = MongoRequest.newRequest("kitpvp", id)
                    .put("last_kit_name", lastKit.getName())
                    .put("deaths", statistics.getDeaths())
                    .put("event_wins", statistics.getEventWins())
                    .put("highest_kill_streak", statistics.getHighestKillStreak())
                    .put("kills", statistics.getKills())
                    .put("kill_streak", statistics.getKillStreak())
                    .put("credits", statistics.getCredits());

            request.run();
        };

        if (async) {
            plugin.getServer().getScheduler().runTaskAsynchronously(plugin, runnable);
        } else {
            runnable.run();
        }
    }

    public void setKit(Kit kit) {
        this.currentKit = this.lastKit = kit;
    }
}
