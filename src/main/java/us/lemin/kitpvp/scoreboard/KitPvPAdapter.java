package us.lemin.kitpvp.scoreboard;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import tech.coords.scoreboardapi.ScoreboardUpdateEvent;
import tech.coords.scoreboardapi.api.ScoreboardAdapter;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.KitPvPPlugin;
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
        PlayerStatistics stats = profile.getStatistics();

        event.addLine(CC.PRIMARY + "Kills: " + CC.SECONDARY + stats.getKills());
        event.addLine(CC.PRIMARY + "Deaths: " + CC.SECONDARY + stats.getDeaths());
        event.addLine(CC.PRIMARY + "Kill Streak: " + CC.SECONDARY + stats.getKillStreak());
        event.addLine(CC.PRIMARY + "KDR: " + CC.SECONDARY + stats.getKillDeathRatio());
        event.addLine(CC.PRIMARY + "Pesos: " + CC.SECONDARY + stats.getPesos());
        event.addLine("");
        event.addLine(CC.SECONDARY + "lemin.us");
    }

    @Override
    public int updateRate() {
        return 20;
    }
}
