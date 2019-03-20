package us.lemin.kitpvp.managers;

import us.lemin.core.utils.time.TimeUtil;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.server.ServerStage;

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

    public String getTimeUntilNextState() {
        long currentTime = System.currentTimeMillis();
        long timeUntilNextState = startTime + serverStage.getDuration();
        return TimeUtil.formatTimeMillisToClock(timeUntilNextState - currentTime);
    }
}
