package rip.titan.archergames.inventory;

import org.bukkit.entity.Player;
import rip.titan.archergames.ArcherGamesPlugin;
import us.lemin.core.api.inventoryapi.InventoryWrapper;
import us.lemin.core.api.inventoryapi.PlayerAction;
import us.lemin.core.api.inventoryapi.PlayerInventoryWrapper;

public class ShopWrapper extends PlayerInventoryWrapper {
    private final ArcherGamesPlugin plugin;

    public ShopWrapper(ArcherGamesPlugin plugin) {
        super("Shop", 6);
        this.plugin = plugin;
    }


    @Override
    public void init(Player player, InventoryWrapper inventoryWrapper) {
        fill(player, inventoryWrapper);
    }

    @Override
    public void update(Player player, InventoryWrapper inventoryWrapper) {
        fill(player, inventoryWrapper);
    }

    private void fill(Player player, InventoryWrapper inventoryWrapper) {
        plugin.getShopManager().getShopKits().values().forEach(shopKit ->
                inventoryWrapper.addItem(shopKit.getKit().getIcon(), new PlayerAction(player1 -> {
                    shopKit.purchase(player);
                    update(player, inventoryWrapper);

                }, false)));
        plugin.getShopManager().getShopItems().values().forEach(shopItem ->
                inventoryWrapper.addItem(shopItem.getItemStack(), new PlayerAction(player1 -> {
                    shopItem.purchase(player);
                    update(player, inventoryWrapper);

                }, false)));
    }
}
