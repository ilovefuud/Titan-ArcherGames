package rip.titan.archergames.managers;

import rip.titan.archergames.kit.Kit;
import rip.titan.archergames.kit.impl.God;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class KitManager {
    private final Map<String, Kit> kitNames = new LinkedHashMap<>();
    private final Map<Class<? extends Kit>, Kit> kitClasses = new LinkedHashMap<>();

    public KitManager() {
        registerKits(
                new God()
        );
    }

    private void registerKits(Kit... kits) {
        for (Kit kit : kits) {
            kitNames.put(kit.getClass().getSimpleName().toLowerCase(), kit);
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
