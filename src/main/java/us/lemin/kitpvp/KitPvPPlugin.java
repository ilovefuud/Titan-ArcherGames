package us.lemin.kitpvp;

import lombok.Getter;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.lemin.kitpvp.commands.KitCommand;
import us.lemin.kitpvp.commands.StatisticsCommand;
import us.lemin.kitpvp.listeners.PlayerListener;
import us.lemin.kitpvp.managers.KitManager;
import us.lemin.kitpvp.managers.ProfileManager;

@Getter
public class KitPvPPlugin extends JavaPlugin {
    private ProfileManager profileManager;
    private KitManager kitManager;

    @Override
    public void onEnable() {
        profileManager = new ProfileManager(this);
        kitManager = new KitManager(this);

        registerCommands(
                new StatisticsCommand(this),
                new KitCommand(this)
        );
        registerListeners(
                new PlayerListener(this)
        );

        World mainWorld = getServer().getWorlds().get(0);

        mainWorld.setGameRuleValue("doFireTick", "false");
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
}
