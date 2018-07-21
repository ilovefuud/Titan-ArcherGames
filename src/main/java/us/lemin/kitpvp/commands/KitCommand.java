package us.lemin.kitpvp.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.kit.Kit;

public class KitCommand extends PlayerCommand implements Listener {
    private final KitPvPPlugin plugin;

    public KitCommand(KitPvPPlugin plugin) {
        super("kit");
        this.plugin = plugin;
        // TODO: fix this aids
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            openKitGui(player);
            return;
        }

        Kit kit = plugin.getKitManager().getKitByName(args[0]);

        if (kit == null) {
            openKitGui(player);
            return;
        }

        kit.apply(player);
    }

    private void openKitGui(Player player) {
        // TODO: use the custom GUI api
        Inventory inventory = plugin.getServer().createInventory(null, 9, "Kit Selector");
        int count = 0;

        for (Kit kit : plugin.getKitManager().getKits()) {
            inventory.setItem(count++, kit.getIcon());
        }

        player.openInventory(inventory);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getCurrentItem() == null) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();

        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().hasDisplayName()) {
            return;
        }

        String name = ChatColor.stripColor(itemStack.getItemMeta().getDisplayName());
        Kit kit = plugin.getKitManager().getKitByName(name);

        if (kit != null) {
            player.closeInventory();
            kit.apply(player);
        }
    }
}
