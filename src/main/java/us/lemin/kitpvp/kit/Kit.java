package us.lemin.kitpvp.kit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;

public abstract class Kit implements Listener {
    private final String name;
    private final ItemStack icon;
    private final KitContents contents;

    public Kit(JavaPlugin plugin, String name, Material icon, String... description) {
        this.name = name;
        this.icon = new ItemBuilder(icon).name(CC.ACCENT + name).lore(description).build();
        this.contents = getContents().build();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public final String getName() {
        return name;
    }

    public final ItemStack getIcon() {
        return icon;
    }

    public void apply(Player player) {
        PlayerInventory inventory = player.getInventory();

        inventory.setArmorContents(contents.getArmor());
        inventory.setContents(contents.getContents());

        onEquip(player);

        player.updateInventory();
        player.sendMessage(CC.PRIMARY + "You have equipped the " + CC.SECONDARY + name + CC.PRIMARY + " kit.");
    }

    protected abstract void onEquip(Player player);

    protected abstract ContentsBuilder getContents();
}
