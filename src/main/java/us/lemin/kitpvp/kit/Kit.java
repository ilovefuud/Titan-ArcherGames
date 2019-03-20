package us.lemin.kitpvp.kit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.ArcherGamesPlugin;

public abstract class Kit implements Listener {
    protected final ArcherGamesPlugin plugin;
    private final String name;
    private final ItemStack icon;
    private final KitContents contents;

    public Kit(ArcherGamesPlugin plugin, String name, ItemStack icon, int cost, String... description) {
        this.plugin = plugin;
        this.name = name;


        ItemBuilder builder = ItemBuilder.from(icon);
        String[] coloredDescription = new String[description.length];

        for (int i = 0; i < description.length; i++) {
            coloredDescription[i] = CC.PRIMARY + description[i];
        }

        this.icon = builder.name(CC.SECONDARY + name).lore(coloredDescription).build();
        this.contents = contentsBuilder().build();

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public Kit(ArcherGamesPlugin plugin, String name, Material icon, int cost, String... description) {
        this(plugin, name, new ItemStack(icon), cost, description);
    }


    public void apply(Player player) {
        contents.apply(player);


        onEquip(player);
        player.sendMessage(CC.PRIMARY + "You have been given the " + CC.SECONDARY + name + CC.PRIMARY + " kit.");
    }

    protected abstract void onEquip(Player player);

    protected abstract KitContents.Builder contentsBuilder();

    public String getName() {
        return name;
    }

    public ItemStack getIcon() {
        return icon;
    }
}
