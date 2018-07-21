package us.lemin.kitpvp.kit.impl;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.kit.ContentsBuilder;
import us.lemin.kitpvp.kit.Kit;

public class PvP extends Kit {
    public PvP(JavaPlugin plugin) {
        super(plugin, "PvP", Material.DIAMOND_SWORD, CC.YELLOW + "The standard PvP kit.");
    }

    @Override
    public void onEquip(Player player) {
        // NO-OP
    }

    @Override
    public ContentsBuilder getContents() {
        ContentsBuilder builder = new ContentsBuilder();

        builder.addItem(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).build());

        for (int i = 0; i < 35; i++) {
            builder.addItem(new ItemStack(Material.MUSHROOM_SOUP));
        }

        builder.withArmor(
                Arrays.asList(
                        new ItemStack(Material.IRON_BOOTS),
                        new ItemStack(Material.IRON_LEGGINGS),
                        new ItemStack(Material.IRON_CHESTPLATE),
                        new ItemStack(Material.IRON_HELMET)
                )
        );

        return builder;
    }
}
