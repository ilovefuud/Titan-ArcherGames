package rip.titan.archergames;

import lombok.Getter;
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
import rip.titan.archergames.commands.HelpCommand;
import rip.titan.archergames.commands.KitCommand;
import rip.titan.archergames.inventory.ShopWrapper;
import rip.titan.archergames.listeners.EntityListener;
import rip.titan.archergames.listeners.PlayerListener;
import rip.titan.archergames.listeners.SpectatorListener;
import rip.titan.archergames.listeners.WorldListener;
import rip.titan.archergames.managers.PlayerManager;
import rip.titan.archergames.managers.ServerManager;
import rip.titan.archergames.managers.ShopManager;
import rip.titan.archergames.scoreboard.AGAdapter;
import rip.titan.archergames.tasks.StageTask;
import rip.titan.archergames.util.structure.Cuboid;
import us.lemin.core.api.inventoryapi.InventoryManager;
import us.lemin.core.api.scoreboardapi.ScoreboardApi;

import java.lang.reflect.Field;

@Getter
public class ArcherGamesPlugin extends JavaPlugin {
    @Getter
    private static ArcherGamesPlugin instance;

    private PlayerManager playerManager;
    private InventoryManager inventoryManager;
    private ServerManager serverManager;
    private ShopManager shopManager;

    private ScoreboardApi scoreboardApi;

    private void registerSerializableClass(Class<?> clazz) {
        if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
            Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
            ConfigurationSerialization.registerClass(serializable);
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        registerSerializableClass(Cuboid.class);

        playerManager = new PlayerManager(this);
        inventoryManager = new InventoryManager(this);
        inventoryManager.registerPlayerWrapper(new ShopWrapper(this));
        serverManager = new ServerManager(this);
        shopManager = new ShopManager(this);

        scoreboardApi = new ScoreboardApi(this, new AGAdapter(this));

        registerCommands(
                new KitCommand(this),
                new HelpCommand()
        );
        registerListeners(
                new PlayerListener(this),
                new WorldListener(),
                new EntityListener(this),
                new SpectatorListener(this)
        );

        BukkitTask stageTask = new StageTask(this).runTaskTimer(this, 20 * 120, 20 * 120);
    }

    @Override
    public void onDisable() {
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
        try {
            Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            final boolean accessible = commandMapField.isAccessible();

            commandMapField.setAccessible(true);

            CommandMap commandMap = (CommandMap) commandMapField.get(getServer());

            for (Command command : commands) {
                commandMap.register(command.getName(), getName(), command);
            }

            commandMapField.setAccessible(accessible);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("An error occurred while registering commands", e);
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
