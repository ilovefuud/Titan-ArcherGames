package us.lemin.kitpvp.kit.impl;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.kit.Kit;
import us.lemin.kitpvp.kit.KitContents;

public class Archer extends Kit {
    public Archer(KitPvPPlugin plugin) {
        super(plugin, "Archer", Material.BOW, "Shoot players.");
    }

    @Override
    protected void onEquip(Player player) {
        // NO-OP
    }

    @Override
    public List<PotionEffect> effects() {
        return Arrays.asList(
                new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1),
                new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, 0)
        );
    }

    @Override
    protected KitContents.Builder contentsBuilder() {
        KitContents.Builder builder = KitContents.newBuilder();

        builder.addItem(new ItemBuilder(Material.IRON_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).build());
        builder.addItem(new ItemBuilder(Material.BOW)
                .enchant(Enchantment.ARROW_DAMAGE, 5)
                .enchant(Enchantment.ARROW_INFINITE, 1)
                .enchant(Enchantment.ARROW_FIRE, 1)
                .enchant(Enchantment.ARROW_KNOCKBACK, 2).build());
        builder.addItem(new ItemStack(Material.ENDER_PEARL, 16));
        builder.setItem(35, new ItemStack(Material.ARROW));
        builder.fill(new ItemStack(Material.MUSHROOM_SOUP));
        builder.addArmor(
                new ItemStack(Material.LEATHER_BOOTS),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.LEATHER_HELMET)
        );

        return builder;
    }
}
