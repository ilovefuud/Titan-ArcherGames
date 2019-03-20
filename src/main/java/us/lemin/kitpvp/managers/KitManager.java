package us.lemin.kitpvp.managers;

import lombok.Getter;
import us.lemin.kitpvp.ArcherGamesPlugin;
import us.lemin.kitpvp.kit.Kit;
import us.lemin.kitpvp.kit.impl.God;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class KitManager {
    private final Map<String, Kit> kitNames = new LinkedHashMap<>();
    private final Map<Class<? extends Kit>, Kit> kitClasses = new LinkedHashMap<>();
    @Getter
    private final Kit defaultKit;

    public KitManager(ArcherGamesPlugin plugin) {
        registerKits(
                new God(plugin)
        );

        defaultKit = getKitByClass(God.class);
    }

    private void registerKits(Kit... kits) {
        for (Kit kit : kits) {
            kitNames.put(kit.getName().toLowerCase(), kit);
            kitClasses.put(kit.getClass(), kit);
        }
    }

    public Kit getKitByName(String kitName) {
        return kitNames.get(kitName.toLowerCase());
    }

    public Kit getKitByClass(Class<? extends Kit> clazz) {
        return kitClasses.get(clazz);
    }

    public Collection<Kit> getKits() {
        return kitNames.values();
    }
}
