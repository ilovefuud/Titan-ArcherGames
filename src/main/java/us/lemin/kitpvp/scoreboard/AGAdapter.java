package us.lemin.kitpvp.scoreboard;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import us.lemin.core.api.scoreboardapi.ScoreboardUpdateEvent;
import us.lemin.core.api.scoreboardapi.api.ScoreboardAdapter;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.managers.ServerManager;
import us.lemin.kitpvp.player.PlayerProfile;
import us.lemin.kitpvp.server.ServerStage;

@RequiredArgsConstructor
public class AGAdapter implements ScoreboardAdapter {
    public AGAdapter(ArcherGamesPlugin plugin) {
        this.plugin = plugin;
    }

    private final ArcherGamesPlugin plugin;

    @Override
    public void onUpdate(ScoreboardUpdateEvent event) {
        event.setTitle(CC.PRIMARY + "Titan " + CC.GRAY + CC.SPLITTER + CC.SECONDARY + " ArcherGames");
        event.setSeparator(CC.BOARD_SEPARATOR);
        Player player = event.getPlayer();
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player);
        ServerManager serverManager = plugin.getServerManager();
        ServerStage serverStage = serverManager.getServerStage();
        int onlinePlayers = (int) plugin.getServer().getOnlinePlayers().stream().count();
        switch (serverStage) {
            case STARTING:
                event.addLine(CC.PRIMARY + "Starting in: " + CC.SECONDARY + serverManager.getTimeUntilNextState());
                event.addLine(CC.PRIMARY + "Online Players: " + CC.SECONDARY + onlinePlayers);
                event.addLine("");
                event.addLine(CC.SECONDARY + "titan.rip");
            case GRACE:
                event.addLine(CC.PRIMARY + "Grace ends in: " + CC.SECONDARY + serverManager.getTimeUntilNextState());
                event.addLine(CC.PRIMARY + "Players left: " + CC.SECONDARY + plugin.getPlayerManager().getContestants());
                event.addLine("");
                event.addLine(CC.SECONDARY + "titan.rip");
                break;
            case FIGHTING:
                event.addLine(CC.PRIMARY + "Kills: " + CC.SECONDARY + profile.getKills());
                event.addLine(CC.PRIMARY + "Players left: " + CC.SECONDARY + plugin.getPlayerManager().getContestants());
                event.addLine("");
                event.addLine(CC.SECONDARY + "titan.rip");
        }
    }

    @Override
    public int updateRate() {
        return 20;
    }
}
