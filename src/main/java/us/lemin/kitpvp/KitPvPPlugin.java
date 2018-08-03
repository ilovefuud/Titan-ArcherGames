package us.lemin.kitpvp;

import lombok.Getter;
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
import tech.coords.inventoryapi.InventoryManager;
import tech.coords.scoreboardapi.ScoreboardApi;
import us.lemin.core.storage.flatfile.Config;
import us.lemin.kitpvp.commands.ClearKitCommand;
import us.lemin.kitpvp.commands.HelpCommand;
import us.lemin.kitpvp.commands.KitCommand;
import us.lemin.kitpvp.commands.SpawnCommand;
import us.lemin.kitpvp.commands.StatisticsCommand;
import us.lemin.kitpvp.commands.admin.EditRegionCommand;
import us.lemin.kitpvp.commands.admin.SetSpawnCommand;
import us.lemin.kitpvp.inventory.KitSelectorWrapper;
import us.lemin.kitpvp.listeners.EntityListener;
import us.lemin.kitpvp.listeners.InventoryListener;
import us.lemin.kitpvp.listeners.PlayerListener;
import us.lemin.kitpvp.listeners.RegionListener;
import us.lemin.kitpvp.listeners.WorldListener;
import us.lemin.kitpvp.managers.KitManager;
import us.lemin.kitpvp.managers.PlayerManager;
import us.lemin.kitpvp.managers.RegionManager;
import us.lemin.kitpvp.scoreboard.KitPvPAdapter;
import us.lemin.kitpvp.util.structure.Cuboid;

@Getter
public class KitPvPPlugin extends JavaPlugin {
    private Config locationConfig;

    @Setter
    private Location spawnLocation;
    @Setter
    private Cuboid spawnCuboid;

    private PlayerManager playerManager;
    private KitManager kitManager;
    private InventoryManager inventoryManager;
    private RegionManager regionManager;

    private ScoreboardApi scoreboardApi;

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
        spawnCuboid = (Cuboid) locationConfig.get("spawn-cuboid");

        playerManager = new PlayerManager(this);
        kitManager = new KitManager(this);
        inventoryManager = new InventoryManager(this);
        inventoryManager.registerWrapper(new KitSelectorWrapper(this));
        regionManager = new RegionManager();

        scoreboardApi = new ScoreboardApi(this, new KitPvPAdapter(this));

        registerCommands(
                new StatisticsCommand(this),
                new KitCommand(this),
                new ClearKitCommand(this),
                new HelpCommand(),
                new SetSpawnCommand(this),
                new EditRegionCommand(this),
                new SpawnCommand(this)
        );
        registerListeners(
                new PlayerListener(this),
                new WorldListener(),
                new InventoryListener(),
                new EntityListener(this),
                new RegionListener(this)
        );

        disableGameRules(mainWorld,
                "doDaylightCycle",
                "doFireTick",
                "doMobSpawning",
                "showDeathMessages"
        );
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
}
