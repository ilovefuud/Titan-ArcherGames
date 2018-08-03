package us.lemin.kitpvp.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
// TODO: make location management
public class TempLocations {
    private static final World MAIN_WORLD = Bukkit.getWorlds().get(0);
    public static final Location SUMO_SPAWN = new Location(MAIN_WORLD, 0.5, 68.0, 1982.5, 0, 0);
    public static final Location SUMO_SPAWN_A = new Location(MAIN_WORLD, 0.5, 68.0, 1994.5, 0, 0);
    public static final Location SUMO_SPAWN_B = new Location(MAIN_WORLD, 0.5, 68.0, 2006.5, 180, 0);
}
