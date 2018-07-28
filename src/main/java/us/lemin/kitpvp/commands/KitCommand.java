package us.lemin.kitpvp.commands;

import org.bukkit.entity.Player;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.inventory.KitSelectorWrapper;
import us.lemin.kitpvp.kit.Kit;
import us.lemin.kitpvp.player.PlayerKitProfile;

public class KitCommand extends PlayerCommand {
    private final KitPvPPlugin plugin;

    public KitCommand(KitPvPPlugin plugin) {
        super("kit");
        this.plugin = plugin;
    }

    @Override
    public void execute(Player player, String[] args) {
        PlayerKitProfile profile = plugin.getProfileManager().getProfile(player);

        if (profile.getKit() != null) {
            player.sendMessage(CC.RED + "You already have a kit!");
            return;
        }

        if (args.length < 1) {
            plugin.getInventoryManager().getWrapper(KitSelectorWrapper.class).open(player);
            return;
        }

        Kit kit = plugin.getKitManager().getKitByName(args[0]);

        if (kit == null) {
            plugin.getInventoryManager().getWrapper(KitSelectorWrapper.class).open(player);
            return;
        }

        kit.apply(player);
    }
}
