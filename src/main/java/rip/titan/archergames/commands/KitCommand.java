package rip.titan.archergames.commands;

import org.bukkit.entity.Player;
import rip.titan.archergames.ArcherGamesPlugin;
import rip.titan.archergames.inventory.ShopWrapper;
import rip.titan.archergames.player.PlayerProfile;
import rip.titan.archergames.server.ServerStage;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.utils.message.CC;

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
