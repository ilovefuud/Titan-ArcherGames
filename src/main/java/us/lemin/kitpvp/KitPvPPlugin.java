package us.lemin.kitpvp;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tech.coords.inventoryapi.InventoryManager;
import tech.coords.scoreboardapi.ScoreboardApi;
import us.lemin.kitpvp.commands.HelpCommand;
import us.lemin.kitpvp.commands.KitCommand;
import us.lemin.kitpvp.commands.StatisticsCommand;
import us.lemin.kitpvp.inventory.KitSelectorWrapper;
import us.lemin.kitpvp.listeners.EntityListener;
import us.lemin.kitpvp.listeners.InventoryListener;
import us.lemin.kitpvp.listeners.PlayerListener;
import us.lemin.kitpvp.listeners.WorldListener;
import us.lemin.kitpvp.managers.KitManager;
import us.lemin.kitpvp.managers.ProfileManager;
import us.lemin.kitpvp.scoreboard.KitPvPAdapter;

@Getter
public class KitPvPPlugin extends JavaPlugin {
    private ProfileManager profileManager;
    private KitManager kitManager;
    private InventoryManager inventoryManager;
    private ScoreboardApi scoreboardApi;

    @Override
    public void onEnable() {
        profileManager = new ProfileManager(this);
        kitManager = new KitManager(this);
        inventoryManager = new InventoryManager(this);
        inventoryManager.registerWrapper(new KitSelectorWrapper(this));
        scoreboardApi = new ScoreboardApi(this, new KitPvPAdapter(this));

        registerCommands(
                new StatisticsCommand(this),
                new KitCommand(this),
                new HelpCommand()
        );
        registerListeners(
                new PlayerListener(this),
                new WorldListener(),
                new InventoryListener(),
                new EntityListener(this)
        );

        World mainWorld = getServer().getWorlds().get(0);

        disableGameRules(mainWorld,
                "doDaylightCycle",
                "doFireTick",
                "doMobSpawning",
                "showDeathMessages"
        );
    }

    @Override
    public void onDisable() {
        profileManager.saveAllProfiles();

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
