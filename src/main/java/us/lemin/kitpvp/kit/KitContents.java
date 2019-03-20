package us.lemin.kitpvp.kit;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class KitContents {
    public KitContents(ItemStack[] armor, ItemStack[] contents) {
        this.armor = armor;
        this.contents = contents;
    }

    private final ItemStack[] armor;
    private final ItemStack[] contents;


    public static Builder newBuilder() {
        return new Builder();
    }

    void apply(Player player) {
        addToInventory(player);
        player.updateInventory();
    }


    private void addToInventory(Player player) {
        PlayerInventory inventory = player.getInventory();
        ItemStack[] armorContents = inventory.getArmorContents();
        boolean droppedItems = false;
        checkContents(player, armorContents);
        inventory.setArmorContents(null);
        inventory.setArmorContents(this.armor.clone());
        checkContents(player, contents);
    }

    private void checkContents(Player player, ItemStack[] itemStacks) {
        Inventory inventory = player.getInventory();
        boolean droppedItems;
        for (ItemStack itemStack : itemStacks) {
            if (inventory.firstEmpty() == -1) {
                player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
                droppedItems = true;
            } else {
                inventory.setItem(inventory.firstEmpty(), itemStack);
            }
        }
    }

    public static class Builder {
        private final ItemStack[] armor = new ItemStack[4];
        private final ItemStack[] contents = new ItemStack[36];

        private int getFirstEmptyIndex() {
            for (int i = 0; i < contents.length; i++) {
                if (contents[i] == null) {
                    return i;
                }
            }
            return -1;
        }

        public Builder addItem(ItemStack item) {
            int index = getFirstEmptyIndex();

            if (index != -1) {
                contents[index] = item;
            }

            return this;
        }

        public Builder fill(ItemStack item) {
            while (getFirstEmptyIndex() != -1) {
                addItem(item.clone());
            }

            return this;
        }

        public Builder setItem(int index, ItemStack item) {
            contents[index] = item;
            return this;
        }

        public Builder addArmor(ItemStack... armor) {
            System.arraycopy(armor, 0, this.armor, 0, armor.length);
            return this;
        }

        private void fillBlank(ItemStack[] items) {
            for (int i = 0; i < items.length; i++) {
                if (items[i] == null) {
                    items[i] = new ItemStack(Material.AIR);
                }
            }
        }


        KitContents build() {
            fillBlank(armor);
            fillBlank(contents);
            return new KitContents(armor, contents);
        }
    }
}
