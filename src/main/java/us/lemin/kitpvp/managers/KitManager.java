package us.lemin.kitpvp.managers;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.kit.Kit;
import us.lemin.kitpvp.kit.impl.Archer;
import us.lemin.kitpvp.kit.impl.PotPvP;
import us.lemin.kitpvp.kit.impl.PvP;

public class KitManager {
    private final Map<String, Kit> kits = new LinkedHashMap<>();

    public KitManager(KitPvPPlugin plugin) {
        registerKits(
                new PvP(plugin),
                new PotPvP(plugin),
                new Archer(plugin)
        );
    }

    private void registerKits(Kit... kits) {
        for (Kit kit : kits) {
            this.kits.put(kit.getName().toLowerCase(), kit);
        }
    }

    public Kit getKitByName(String kitName) {
        return kits.get(kitName.toLowerCase());
    }

    public Collection<Kit> getKits() {
        return kits.values();
    }
}
