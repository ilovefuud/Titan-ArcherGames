package rip.titan.archergames.scoreboard;

import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import rip.titan.archergames.ArcherGamesPlugin;
import rip.titan.archergames.managers.ServerManager;
import rip.titan.archergames.player.PlayerProfile;
import rip.titan.archergames.server.ServerStage;
import us.lemin.core.api.scoreboardapi.ScoreboardUpdateEvent;
import us.lemin.core.api.scoreboardapi.api.ScoreboardAdapter;
import us.lemin.core.utils.message.CC;

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
                event.addLine(CC.PRIMARY + "Starting in: " + CC.SECONDARY + serverManager.getTimeUntilNextStage());
                event.addLine(CC.PRIMARY + "Online Players: " + CC.SECONDARY + onlinePlayers);
                event.addLine("");
                event.addLine(CC.SECONDARY + "titan.rip");
            case GRACE:
                event.addLine(CC.PRIMARY + "Grace ends in: " + CC.SECONDARY + serverManager.getTimeUntilNextStage());
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
