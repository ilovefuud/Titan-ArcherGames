package us.lemin.kitpvp.kit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.inventory.ItemStack;

@Getter
@RequiredArgsConstructor
public class KitContents {
    private final ItemStack[] armor;
    private final ItemStack[] contents;
}
