package us.lemin.kitpvp;

import lombok.Getter;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import us.lemin.kitpvp.listeners.PlayerListener;
import us.lemin.kitpvp.managers.ProfileManager;

@Getter
public class KitPvPPlugin extends JavaPlugin {
    private ProfileManager profileManager;

    @Override
    public void onEnable() {
        profileManager = new ProfileManager(this);

        PluginManager pluginManager = getServer().getPluginManager();

        pluginManager.registerEvents(new PlayerListener(this), this);
    }
}
