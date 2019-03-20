package us.lemin.kitpvp;

import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import us.lemin.core.api.inventoryapi.InventoryManager;
import us.lemin.core.api.scoreboardapi.ScoreboardApi;
import us.lemin.core.storage.flatfile.Config;
import us.lemin.kitpvp.commands.HelpCommand;
import us.lemin.kitpvp.commands.KitCommand;
import us.lemin.kitpvp.commands.admin.SetSpawnCommand;
import us.lemin.kitpvp.inventory.ShopWrapper;
import us.lemin.kitpvp.listeners.*;
import us.lemin.kitpvp.managers.KitManager;
import us.lemin.kitpvp.managers.PlayerManager;
import us.lemin.kitpvp.managers.ServerManager;
import us.lemin.kitpvp.managers.ShopManager;
import us.lemin.kitpvp.scoreboard.AGAdapter;
import us.lemin.kitpvp.tasks.StageTask;
import us.lemin.kitpvp.util.structure.Cuboid;

public class ArcherGamesPlugin extends JavaPlugin {
    private Config locationConfig;

    @Setter
    private Location spawnLocation;

    private PlayerManager playerManager;
    private KitManager kitManager;
    private InventoryManager inventoryManager;
    private ServerManager serverManager;
    private ShopManager shopManager;

    private ScoreboardApi scoreboardApi;

    private BukkitTask stageTask;

    private void registerSerializableClass(Class<?> clazz) {
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.registerClass(serializable);
        }
    }

    @Override
    public void onEnable() {
        registerSerializableClass(Cuboid.class);

        locationConfig = new Config(this, "locations");

        World mainWorld = getServer().getWorlds().get(0);

        locationConfig.addDefault("spawn", mainWorld.getSpawnLocation());
        locationConfig.addDefault("spawn-cuboid", new Cuboid(mainWorld.getSpawnLocation()));
        locationConfig.copyDefaults();

        spawnLocation = locationConfig.getLocation("spawn");

        playerManager = new PlayerManager(this);
        kitManager = new KitManager(this);
        inventoryManager = new InventoryManager(this);
        inventoryManager.registerPlayerWrapper(new ShopWrapper(this));
        serverManager = new ServerManager(this);
        shopManager = new ShopManager(this);

        scoreboardApi = new ScoreboardApi(this, new AGAdapter(this));

        registerCommands(
                new KitCommand(this),
                new HelpCommand(),
                new SetSpawnCommand(this)
        );
        registerListeners(
                new PlayerListener(this),
                new WorldListener(),
                new InventoryListener(),
                new EntityListener(this),
                new SpectatorListener(this)
        );

        disableGameRules(mainWorld,
                "doFireTick",
                "doMobSpawning"
        );
        stageTask = new StageTask(this).runTaskTimer(this, 20 * 120, 20 * 120);
    }

    @Override
    public void onDisable() {
        locationConfig.save();
        playerManager.saveAllProfiles();

        World mainWorld = getServer().getWorlds().get(0);

        for (Entity entity : mainWorld.getEntities()) {
            if (entity instanceof Player) {
                continue;
            }

            entity.remove();
        }
    }

    private void registerCommands(Command... commands) {
        CommandMap commandMap = getServer().getCommandMap();

        for (Command command : commands) {
            commandMap.register(getName(), command);
        }
    }

    private void registerListeners(Listener... listeners) {
        PluginManager pluginManager = getServer().getPluginManager();

        for (Listener listener : listeners) {
            pluginManager.registerEvents(listener, this);
        }
    }

    private void disableGameRules(World world, String... gameRules) {
        for (String gameRule : gameRules) {
            world.setGameRuleValue(gameRule, "false");
        }
    }

    public PlayerManager getPlayerManager() {
        return this.playerManager;
    }

    public ServerManager getServerManager() {
        return serverManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public ShopManager getShopManager() {
        return shopManager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public Config getLocationConfig() {
        return locationConfig;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }
}
