package us.lemin.kitpvp.kit;

import java.util.List;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import us.lemin.core.utils.item.ItemBuilder;
import us.lemin.core.utils.message.CC;
import us.lemin.kitpvp.KitPvPPlugin;
import us.lemin.kitpvp.player.PlayerKitProfile;

public abstract class Kit implements Listener {
    protected final KitPvPPlugin plugin;
    @Getter
    private final String name;
    @Getter
    private final ItemStack icon;
    private final KitContents contents;

    public Kit(KitPvPPlugin plugin, String name, ItemStack icon, String... description) {
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

    public Kit(KitPvPPlugin plugin, String name, Material icon, String... description) {
        this(plugin, name, new ItemStack(icon), description);
    }

    protected boolean checkPlayer(Player player) {
        PlayerKitProfile profile = plugin.getPlayerManager().getProfile(player);
        return profile.getKit() == this;
    }

    public void apply(Player player) {
        PlayerKitProfile profile = plugin.getPlayerManager().getProfile(player);

        profile.setKit(this);
        contents.apply(player);

        for (PotionEffect effect : player.getActivePotionEffects()) {
            player.removePotionEffect(effect.getType());
        }

        for (PotionEffect effect : effects()) {
            player.addPotionEffect(effect);
        }

        onEquip(player);
        player.sendMessage(CC.PRIMARY + "You have equipped the " + CC.SECONDARY + name + CC.PRIMARY + " kit.");
    }

    protected abstract void onEquip(Player player);

    public abstract List<PotionEffect> effects();

    protected abstract KitContents.Builder contentsBuilder();
}
