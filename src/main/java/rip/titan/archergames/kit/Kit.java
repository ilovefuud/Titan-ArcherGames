package rip.titan.archergames.kit;

import com.google.common.collect.ImmutableList;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor()
public abstract class Kit {
    @Getter
    private final String name;
    @Getter
    private final ItemStack icon;
    private final ItemStack[] inventoryContents;
    private final ItemStack[] armorContents;



    public Kit(String name, ItemStack icon, String... description) {
        this.name = name;

        ItemBuilder builder = ItemBuilder.from(icon);
        String[] coloredDescription = new String[description.length];

        for (int i = 0; i < description.length; i++) {
            coloredDescription[i] = CC.PRIMARY + description[i];
        }

        this.icon = builder.name(CC.SECONDARY + name).lore(coloredDescription).build();

        this.armorContents = setArmor().toArray(new ItemStack[0]);
        this.inventoryContents = setContents().toArray(new ItemStack[0]);
    }

    public Kit(String name, Material icon, String... description) {
        this(name, new ItemStack(icon), description);
    }

    public void equip(Player player) {
        addIfEmpty(player);
    }

    private void addIfEmpty(Player player) {
        final List<ItemStack[]> itemStackList = Arrays.asList(armorContents, inventoryContents);
        Inventory inventory = player.getInventory();

        boolean droppedItems = false;

        for (ItemStack[] itemStackArray : itemStackList) {
            for (ItemStack itemStack : itemStackArray) {
                if (inventory.firstEmpty() == -1) {
                    player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                    droppedItems = true;
                } else {
                    inventory.setItem(inventory.firstEmpty(), itemStack);
                }
            }
        }
        if (droppedItems) {
            player.sendMessage(CC.RED + "Items were dropped at your feet because your inventory was full.");
        }
    }

    protected abstract ImmutableList<ItemStack> setArmor();

    protected abstract ImmutableList<ItemStack> setContents();
}
