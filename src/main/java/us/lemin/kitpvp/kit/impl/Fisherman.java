package us.lemin.kitpvp.kit.impl;

import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.kit.Kit;
import us.lemin.kitpvp.kit.KitContents;

public class Fisherman extends Kit {
    public Fisherman(KitPvPPlugin plugin) {
        super(plugin, "Fisherman", Material.FISHING_ROD, "Fish other players towards you with a rod.");
        registerCooldownTimer("fishing", 15);
    }

    @Override
    protected void onEquip(Player player) {
        // NO-OP
    }

    @Override
    protected List<PotionEffect> effects() {
        return Collections.singletonList(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
    }

    @Override
    protected KitContents.Builder contentsBuilder() {
        KitContents.Builder builder = KitContents.newBuilder();

        builder.addItem(new ItemBuilder(Material.IRON_SWORD).enchant(Enchantment.DAMAGE_ALL, 1).build());
        builder.addItem(new ItemStack(Material.FISHING_ROD));
        builder.fill(new ItemStack(Material.MUSHROOM_SOUP));
        builder.addArmor(
                new ItemStack(Material.GOLD_BOOTS),
                new ItemStack(Material.IRON_LEGGINGS),
                new ItemStack(Material.IRON_CHESTPLATE),
                new ItemStack(Material.GOLD_HELMET)
        );

        return builder;
    }

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (!(event.getCaught() instanceof Player)) {
            return;
        }

        Player fisherman = event.getPlayer();

        if (isInvalidKit(fisherman) || isOnCooldown(fisherman, "fishing")) {
            return;
        }

        event.setCancelled(true);

        Player victim = (Player) event.getCaught();

        victim.teleport(fisherman);
    }
}
