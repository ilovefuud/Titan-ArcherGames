package us.lemin.kitpvp.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.kit.Kit;
import us.lemin.kitpvp.kit.impl.PvP;

public class KitManager {
    @Getter
    private final List<Kit> kits = new ArrayList<>();

    public KitManager(KitPvPPlugin plugin) {
        registerKits(
                new PvP(plugin)
        );
    }

    private void registerKits(Kit... kits) {
        this.kits.addAll(Arrays.asList(kits));
    }

    public Kit getKitByName(String kitName) {
        for (Kit kit : kits) {
            if (kit.getName().equalsIgnoreCase(kitName)) {
                return kit;
            }
        }

        return null;
    }
}
