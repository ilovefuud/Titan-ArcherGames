package us.lemin.kitpvp.commands.admin;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.ArcherGamesPlugin;

public class SetSpawnCommand extends PlayerCommand {
    private final ArcherGamesPlugin plugin;

    public SetSpawnCommand(ArcherGamesPlugin plugin) {
        super("setspawn", Rank.ADMIN);
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        Location location = player.getLocation();

        location.setX(location.getBlockX() + 0.5);
        location.setY(location.getBlockY() + 3.0);
        location.setZ(location.getBlockZ() + 0.5);

        plugin.setSpawnLocation(location);

        plugin.getLocationConfig().set("spawn", location);
        plugin.getLocationConfig().save();

        player.sendMessage(CC.GREEN + "Set the spawn!");
    }
}
