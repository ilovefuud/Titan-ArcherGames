package us.lemin.kitpvp.kit;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.inventory.ItemStack;

public class ContentsBuilder {
    private final List<ItemStack> armor = new ArrayList<>();
    private final List<ItemStack> items = new ArrayList<>();

    public ContentsBuilder setItem(int slot, ItemStack item) {
        items.add(slot, item);
        return this;
    }

    public ContentsBuilder addItem(ItemStack item) {
        items.add(item);
        return this;
    }

    public ContentsBuilder withArmor(List<ItemStack> armor) {
        this.armor.addAll(armor);
        return this;
    }

    public KitContents build() {
        return new KitContents(
                armor.toArray(new ItemStack[0]),
                items.toArray(new ItemStack[0])
        );
    }
}
