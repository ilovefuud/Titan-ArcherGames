package us.lemin.kitpvp.inventory;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.coords.inventoryapi.PlayerAction;
import tech.coords.inventoryapi.SimpleInventoryWrapper;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.kit.Kit;

public class KitSelectorWrapper extends SimpleInventoryWrapper {
    private final KitPvPPlugin plugin;

    public KitSelectorWrapper(KitPvPPlugin plugin) {
        super("Kit Selector", 3);
        this.plugin = plugin;
    }

    @Override
    public void init() {
        // fill border with orange glass
        for (int row = 1; row <= 3; row++) {
            for (int column = 1; column <= 9; column++) {
                if (row != 2 || column == 1 || column == 9) {
                    setItem(row, column, new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 1), e -> {
                    });
                }
            }
        }

        int count = 2;

        for (Kit kit : plugin.getKitManager().getKits()) {
            setItem(2, count++, kit.getIcon().clone(), new PlayerAction(kit::apply, true));
        }
    }

    @Override
    public void update() {
    }
}
