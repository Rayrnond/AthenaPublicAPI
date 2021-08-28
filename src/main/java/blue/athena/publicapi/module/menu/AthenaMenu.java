package blue.athena.publicapi.module.menu;

import blue.athena.publicapi.module.menu.button.Button;
import blue.athena.publicapi.AthenaPublicAPI;
import blue.athena.publicapi.misc.java.AthenaString;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class AthenaMenu {

    @Getter private final Player player;

    @Getter @Setter private Inventory inventory;
    @Getter private final HashMap<Integer, Button> buttons;

    public AthenaMenu(Player player, int rows, String name) {

        this.player = player;

        inventory = Bukkit.createInventory(null, rows*9, AthenaString.fullyProcess(name, player));
        buttons = new HashMap<>();

    }

    public Button getButton(int i) {

        return buttons.get(i);

    }

    public void setButton(int i, Button button) {

        buttons.put(i, button);

    }

    public void setEmpty(int i, ItemStack item) {

        buttons.put(i, new Button() {
            @Override
            public void onClick(InventoryClickEvent event, AthenaMenu menu) {
                event.setCancelled(true);
            }
        });
        inventory.setItem(i, item);

    }

    public void fillAllEmpty(ItemStack item) {

        for (int i = 0; i != inventory.getSize(); i++) {

            if (!buttons.containsKey(i)) setEmpty(i, item);;

        }

    }
    public void fillEmptyRow(int row, ItemStack item) {

        int first = (row-1)*9;
        int last = (row*9)-1;

        while (first != last) {

            setEmpty(first, item);
            first++;

        }

    }
    public void fillEmptyColumn(int column, ItemStack item) {

        if (column > 8) return;
        column++;

        for (int i = 0; i < inventory.getSize()/9; i++) {

            setEmpty(column+(9*i), item);

        }

    }

    public void open() {

        player.openInventory(inventory);

    }
    public void closeThenOpen() {

        player.closeInventory();
        new BukkitRunnable() {
            @Override
            public void run() {

                player.openInventory(inventory);

            }

        }.runTaskLater(AthenaPublicAPI.getAPI().getPlugin(), 1);

    }

}
