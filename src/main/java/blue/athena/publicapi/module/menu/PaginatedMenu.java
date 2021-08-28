package blue.athena.publicapi.module.menu;

import blue.athena.publicapi.module.menu.button.Button;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PaginatedMenu extends AthenaMenu {

    @Getter private final List<Inventory> inventoryCache;
    @Getter private final List<HashMap<Integer, Button>> mapCache;

    public PaginatedMenu(Player player, int rows, String name) {

        super(player, rows, name);

        inventoryCache = new ArrayList<>();
        mapCache = new ArrayList<>();

    }

    public void setMenu(int menu) {

        getButtons().clear();
        getButtons().putAll(mapCache.get(menu));
        setInventory(getInventoryCache().get(menu));

    }



}
