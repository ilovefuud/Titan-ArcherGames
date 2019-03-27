package rip.titan.archergames.managers;

import rip.titan.archergames.ArcherGamesPlugin;
import rip.titan.archergames.server.ServerStage;
import us.lemin.core.utils.time.TimeUtil;

public class ServerManager {

    private final ArcherGamesPlugin plugin;

    private ServerStage serverStage;
    private Long startTime;

    public ServerManager(ArcherGamesPlugin plugin) {
        this.plugin = plugin;
        this.serverStage = ServerStage.STARTING;
        this.startTime = System.currentTimeMillis();
    }

    public void setServerStage(ServerStage serverStage) {
        this.serverStage = serverStage;
    }

    public ServerStage getServerStage() {
        return serverStage;
    }

    public String getTimeUntilNextStage() {
        long currentTime = System.currentTimeMillis();
        long timeUntilNextState = startTime + serverStage.getDuration();
        return TimeUtil.formatTimeMillisToClock(timeUntilNextState - currentTime);
    }
}
