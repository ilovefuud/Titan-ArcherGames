package us.lemin.kitpvp.scoreboard;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import tech.coords.scoreboardapi.ScoreboardUpdateEvent;
import tech.coords.scoreboardapi.api.ScoreboardAdapter;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.events.Event;
import us.lemin.kitpvp.events.EventStage;
import us.lemin.kitpvp.player.PlayerKitProfile;
import us.lemin.kitpvp.player.PlayerStatistics;

@RequiredArgsConstructor
public class KitPvPAdapter implements ScoreboardAdapter {
    private final KitPvPPlugin plugin;

    @Override
    public void onUpdate(ScoreboardUpdateEvent event) {
        event.setTitle(CC.PRIMARY + "Lemin " + CC.GRAY + CC.SPLITTER + CC.SECONDARY + " KitPvP");
        event.setSeparator(CC.BOARD_SEPARATOR);

        Player player = event.getPlayer();
        PlayerKitProfile profile = plugin.getPlayerManager().getProfile(player);

        switch (profile.getState()) {
            case SPAWN:
            case FFA:
                PlayerStatistics stats = profile.getStatistics();

                event.addLine(CC.PRIMARY + "Kills: " + CC.SECONDARY + stats.getKills());
                event.addLine(CC.PRIMARY + "Deaths: " + CC.SECONDARY + stats.getDeaths());
                event.addLine(CC.PRIMARY + "Kill Streak: " + CC.SECONDARY + stats.getKillStreak());
                event.addLine(CC.PRIMARY + "KDR: " + CC.SECONDARY + stats.getKillDeathRatio());
                event.addLine(CC.PRIMARY + "Credits: " + CC.SECONDARY + stats.getCredits());
                event.addLine("");
                event.addLine(CC.SECONDARY + "lemin.us");
                break;
            case EVENT:
                Event activeEvent = profile.getActiveEvent();

                if (activeEvent == null) {
                    return;
                }

                if (activeEvent.getCurrentStage() == EventStage.FIGHTING) {
                    Player first = plugin.getServer().getPlayer(activeEvent.remainingFighterIds().get(0));

                    if (first == null) {
                        return;
                    }

                    Player second = plugin.getServer().getPlayer(activeEvent.remainingFighterIds().get(1));

                    if (second == null) {
                        return;
                    }

                    event.addLine(CC.PRIMARY + first.getName() + CC.SECONDARY + " (" + first.spigot().getPing() + " ms)");
                    event.addLine(CC.ACCENT + "vs.");
                    event.addLine(CC.PRIMARY + second.getName() + CC.SECONDARY + " (" + second.spigot().getPing() + " ms)");
                } else if (activeEvent.getCurrentStage() == EventStage.INTERMISSION) {
                    event.addLine(CC.PRIMARY + "Remaining: " + CC.SECONDARY + (activeEvent.remainingPlayerIds().size() + activeEvent.remainingFighterIds().size()));
                } else if (activeEvent.getCurrentStage() == EventStage.WAITING_FOR_PLAYERS) {
                    event.addLine(CC.PRIMARY + "Playing: " + CC.SECONDARY + activeEvent.remainingPlayerIds().size());
                }
                break;
        }
    }

    @Override
    public int updateRate() {
        return 20;
    }
}
