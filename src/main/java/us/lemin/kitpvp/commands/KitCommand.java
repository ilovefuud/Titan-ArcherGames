package us.lemin.kitpvp.commands;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import us.lemin.core.commands.PlayerCommand;
import us.lemin.core.utils.item.ItemBuilder;

public class KitCommand extends PlayerCommand {
    public KitCommand() {
        super("kit");
    }

    @Override
    public void execute(Player player, String[] args) {
        PlayerInventory inventory = player.getInventory();

        inventory.clear();
        inventory.setArmorContents(null);

        inventory.setArmorContents(
                new ItemStack[]{new ItemStack(Material.IRON_BOOTS),
                        new ItemStack(Material.IRON_LEGGINGS),
                        new ItemStack(Material.IRON_CHESTPLATE),
                        new ItemStack(Material.IRON_HELMET)
                }
        );

        inventory.addItem(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).build());

        for (int i = 0; i < inventory.getSize(); i++) {
            inventory.addItem(new ItemStack(Material.MUSHROOM_SOUP));
        }

        player.updateInventory();

        player.sendMessage("fucking kit");
    }
}
