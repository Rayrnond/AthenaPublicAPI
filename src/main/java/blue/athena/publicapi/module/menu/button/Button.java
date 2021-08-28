package blue.athena.publicapi.module.menu.button;

import blue.athena.publicapi.module.menu.AthenaMenu;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class Button {

    public abstract void onClick(InventoryClickEvent event, AthenaMenu menu);

}
