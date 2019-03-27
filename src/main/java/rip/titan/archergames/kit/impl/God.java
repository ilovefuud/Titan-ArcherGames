package rip.titan.archergames.kit.impl;

import com.google.common.collect.ImmutableList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import rip.titan.archergames.kit.Kit;

public class God extends Kit {
    public God() {
        super("God", Material.DIAMOND_SWORD, "The God Kit.");
    }

    @Override
    protected ImmutableList<ItemStack> setArmor() {
        return ImmutableList.of(
                new ItemStack(Material.IRON_BOOTS),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.IRON_HELMET)
        );
    }

    @Override
    protected ImmutableList<ItemStack> setContents() {
        return ImmutableList.of(
                new ItemStack(Material.IRON_SPADE)
        );
    }
}
