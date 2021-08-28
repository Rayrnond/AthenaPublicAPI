package blue.athena.publicapi.module.menu;

import blue.athena.publicapi.AthenaPublicAPI;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;

public class MenuListener implements Listener {

    static {

        Bukkit.getPluginManager().registerEvents(new MenuListener(), AthenaPublicAPI.getAPI().getPlugin());

    }

    @Getter private static final HashMap<Player, AthenaMenu> cache = new HashMap<>();

    @EventHandler
    public void onClick(InventoryClickEvent event) {

        AthenaMenu menu = cache.get((Player) event.getWhoClicked());

        if (menu != null) {

            try {
                menu.getButton(event.getSlot()).onClick(event, menu);
            } catch (NullPointerException ignored) {



            }

        }

    }

}
