package us.lemin.kitpvp.util;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;

@RequiredArgsConstructor
public enum ItemHotbars {
    SPAWN_ITEMS(
            new ItemStack[]{
                    new ItemBuilder(Material.CHEST).name(CC.GOLD + "Kit Selector").build(),
                    null,
                    null,
                    null,
                    new ItemBuilder(Material.PAPER).name(CC.GRAY + "Your Stats").build(),
                    null,
                    null,
                    null,
                    new ItemBuilder(Material.DIAMOND_SWORD).name(CC.GRAY + "Duel Arena").build(),
            }
    );

    private final ItemStack[] hotbar;

    public void apply(Player player) {
        for (int i = 0; i < hotbar.length; i++) {
            ItemStack item = hotbar[i];
            player.getInventory().setItem(i, item == null ? new ItemStack(Material.AIR) : item.clone());
        }

        player.updateInventory();
    }
}
