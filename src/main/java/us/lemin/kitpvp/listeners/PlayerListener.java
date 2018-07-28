package us.lemin.kitpvp.listeners;

import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.player.PlayerUtil;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.player.PlayerDamageData;
import us.lemin.kitpvp.player.PlayerKitProfile;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    private final KitPvPPlugin plugin;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            plugin.getProfileManager().createProfile(event.getUniqueId(), event.getName());
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerKitProfile profile = plugin.getProfileManager().getProfile(player);

        if (profile == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, CC.RED + "Your data failed to load for KitPvP. Try logging in again.");
            return;
        } else if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            plugin.getProfileManager().removeProfile(player);
            return;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        PlayerUtil.clearPlayer(player);

        player.teleport(player.getWorld().getSpawnLocation());

        player.sendMessage(CC.SEPARATOR);
        player.sendMessage(CC.PRIMARY + "Welcome to " + CC.SECONDARY + "Lemin KitPvP" + CC.PRIMARY + "!");
        player.sendMessage(CC.SEPARATOR);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerKitProfile profile = plugin.getProfileManager().getProfile(player);

        profile.save(plugin);
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {

    }

    @EventHandler
    public void onSoup(PlayerInteractEvent event) {
        if (!event.hasItem() || event.getItem().getType() != Material.MUSHROOM_SOUP
                || event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isDead() && player.getHealth() > 0.0 && player.getHealth() <= 19.0) {
            event.setCancelled(true);
            double health = player.getHealth() + 7.0;

            player.setHealth(health > 20.0 ? 20.0 : health);
            player.getItemInHand().setType(Material.BOWL);
            player.updateInventory();
        }
    }

    @EventHandler
    public void onHunger(FoodLevelChangeEvent event) {
        if (event.getFoodLevel() < 20) {
            event.setFoodLevel(20);
        } else {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() != GameMode.CREATIVE) {
            if (event.getItemDrop().getItemStack().getType() != Material.BOWL) {
                event.setCancelled(true);
                return;
            }

            event.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getDrops().clear();

        Player player = event.getEntity();
        PlayerKitProfile profile = plugin.getProfileManager().getProfile(player);
        PlayerDamageData damageData = profile.getDamageData();
        double totalDamage = damageData.total();
        player.sendMessage(totalDamage + "");

        for (Map.Entry<UUID, Double> entry : damageData.sortedMap().entrySet()) {
            UUID damagerId = entry.getKey();
            Player damager = plugin.getServer().getPlayer(damagerId);
            double damage = entry.getValue();
            double percent = damage / totalDamage;
            damager.sendMessage("you did " + percent + "of dmg to player");
        }
        damageData.clear();

        profile.getStatistics().handleDeath();
        profile.setKit(null);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            if (player.isOnline()) {
                player.spigot().respawn();
            }
        }, 16L);
    }
}
