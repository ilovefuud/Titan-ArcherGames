package us.lemin.kitpvp.listeners;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.lemin.core.CorePlugin;
import us.lemin.core.player.CoreProfile;
import us.lemin.core.player.rank.Rank;
import us.lemin.core.utils.message.CC;
import us.lemin.core.utils.player.PlayerUtil;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.managers.PlayerManager;
import us.lemin.kitpvp.managers.ShopManager;
import us.lemin.kitpvp.player.PlayerProfile;
import us.lemin.kitpvp.server.ServerStage;
import us.lemin.kitpvp.shop.ShopEntry;
import us.lemin.kitpvp.shop.impl.ShopItem;
import us.lemin.kitpvp.shop.impl.ShopKit;

@RequiredArgsConstructor
public class PlayerListener implements Listener {
    public PlayerListener(ArcherGamesPlugin plugin) {
        this.plugin = plugin;
    }

    private final ArcherGamesPlugin plugin;

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {
        if (event.getLoginResult() == AsyncPlayerPreLoginEvent.Result.ALLOWED) {
            plugin.getPlayerManager().createProfile(event.getUniqueId(), event.getName());
        }
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player);

        if (profile == null) {
            event.disallow(PlayerLoginEvent.Result.KICK_OTHER, CC.RED + "Your data failed to load for Archer Games. Try logging in again.");
            return;
        } else if (event.getResult() != PlayerLoginEvent.Result.ALLOWED) {
            plugin.getPlayerManager().removeProfile(player);
            return;
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerUtil.clearPlayer(player);
        player.sendMessage(CC.SEPARATOR);
        player.sendMessage(CC.PRIMARY + "Welcome to " + CC.SECONDARY + "Archer Games" + CC.PRIMARY + "!");
        player.sendMessage(CC.SEPARATOR);

        if (plugin.getServerManager().getServerStage() == ServerStage.FIGHTING) {
            plugin.getPlayerManager().removePlayer(player);
        }

    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoinHighest(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Scoreboard scoreboard = player.getScoreboard();

        Team invisTeam = scoreboard.registerNewTeam("SeeInvis");
        invisTeam.setCanSeeFriendlyInvisibles(true);

        for (Rank rank : Rank.values()) {
            Team team = scoreboard.registerNewTeam(rank.getName());
            team.setPrefix(rank.getColor());
        }

        for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
            CoreProfile coreProfile = CorePlugin.getInstance().getProfileManager().getProfile(onlinePlayer.getUniqueId());
            Team team = scoreboard.getTeam(coreProfile.getRank().getName());
            team.addEntry(onlinePlayer.getName());
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerProfile profile = plugin.getPlayerManager().getProfile(player);

        if (profile == null) {
            return;
        }

        switch (plugin.getServerManager().getServerStage()) {
            case GRACE:
            case FIGHTING:
                player.setHealth(0);
                break;
        }

        profile.save(plugin);

        PlayerManager playerManager = plugin.getPlayerManager();

        playerManager.removeSpectator(player);
        playerManager.removeProfile(player);
    }


    @EventHandler
    public void onShopSignCreate(SignChangeEvent event) {
        Player player = event.getPlayer();
        CoreProfile profile = CorePlugin.getInstance().getProfileManager().getProfile(player.getUniqueId());
        if (!profile.hasRank(Rank.ADMIN)) {
            return;
        }

        if (event.getLine(0) == null
                || !event.getLine(0).equalsIgnoreCase("kit")
                || !event.getLine(0).equalsIgnoreCase("item")) {
            player.sendMessage(CC.RED + "Shop Entry type cannot be empty.");
            event.setCancelled(true);
            return;
        }

        String type = event.getLine(0);

        if (event.getLine(1) == null) {
            player.sendMessage(CC.RED + "Shop Entry name cannot be empty.");
            event.setCancelled(true);
            return;
        }


        ShopManager shopManager = plugin.getShopManager();
        ShopEntry shopEntry = null;
        switch (type) {
            case "kit":
                shopEntry = shopManager.getShopKitByName(event.getLine(1));
                break;
            case "item":
                shopEntry = shopManager.getShopItemByName(event.getLine(1));
                break;
        }

        if (shopEntry == null) {
            player.sendMessage(CC.RED + "Shop Entry does not exist.");
            event.setCancelled(true);
        }

        if (shopEntry.getClass().isAssignableFrom(ShopItem.class)) {
            ShopItem shopItem = (ShopItem) shopEntry;
            event.setLine(0, "[Item]");
            event.setLine(1, "Price: " + shopItem.getCost());
            event.setLine(2, shopItem.getName());
        }
        if (shopEntry.getClass().isAssignableFrom(ShopKit.class)) {
            ShopKit shopKit = (ShopKit) shopEntry;
            event.setLine(0, "[Kit]");
            event.setLine(1, "Price: " + shopKit.getCost());
            event.setLine(2, shopKit.getName());
        }
    }

    @EventHandler
    public void onShopSignPurchase(PlayerInteractEvent event){
        Material clickedBlockType = event.getClickedBlock().getType();
        if(clickedBlockType == Material.SIGN
                ||clickedBlockType == Material.SIGN_POST
                ||clickedBlockType == Material.WALL_SIGN){
            if(event.getAction() == Action.RIGHT_CLICK_BLOCK){
                Sign sign = (Sign) event.getClickedBlock().getState();

                ShopManager shopManager = plugin.getShopManager();
                ShopEntry shopEntry = null;

                switch (sign.getLine(0).toLowerCase()) {
                    case "[kit]":
                        shopEntry = shopManager.getShopKitByName(sign.getLine(1));
                        break;
                    case "[item]":
                        shopEntry = shopManager.getShopItemByName(sign.getLine(1));
                        break;
                }

                Player player = event.getPlayer();

                if (shopEntry == null) {
                    player.sendMessage(CC.RED + "Shop Entry does not exist.");
                    event.setCancelled(true);
                }

                if (shopEntry.getClass().isAssignableFrom(ShopItem.class)) {
                    ShopItem shopItem = (ShopItem) shopEntry;
                    shopItem.purchase(player);
                    return;
                }
                if (shopEntry.getClass().isAssignableFrom(ShopKit.class)) {
                    ShopKit shopKit = (ShopKit) shopEntry;
                    shopKit.purchase(player);
                }
            }
        }
    }
}
