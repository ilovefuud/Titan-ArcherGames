package us.lemin.kitpvp.commands;

import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.inventory.ShopWrapper;
import us.lemin.kitpvp.player.PlayerProfile;
import us.lemin.kitpvp.server.ServerStage;

public class KitCommand extends PlayerCommand {
    private final ArcherGamesPlugin plugin;

    public KitCommand(ArcherGamesPlugin plugin) {
        super("kit");
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player);
        if (plugin.getServerManager().getServerStage() != ServerStage.GRACE) {
            player.sendMessage(CC.RED + "You can only use the shop during Grace period.");
            return;
        }
        plugin.getInventoryManager().getPlayerWrapper(ShopWrapper.class).open(player);
    }
}
