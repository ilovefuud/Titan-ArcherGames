package us.lemin.kitpvp.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.server.ServerStage;

public class StageTask extends BukkitRunnable {

    private final ArcherGamesPlugin plugin;

    public StageTask(ArcherGamesPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        switch (plugin.getServerManager().getServerStage()) {
            case STARTING:
                plugin.getServerManager().setServerStage(ServerStage.GRACE);
                break;
            case GRACE:
                plugin.getServerManager().setServerStage(ServerStage.FIGHTING);
                cancel();
                break;
        }
    }

}
