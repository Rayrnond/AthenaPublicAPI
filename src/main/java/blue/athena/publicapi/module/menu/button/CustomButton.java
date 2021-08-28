package blue.athena.publicapi.module.menu.button;

import blue.athena.publicapi.module.menu.AthenaMenu;
import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class CustomButton extends Button {

    @Override
    public void onClick(InventoryClickEvent event, AthenaMenu menu) {

        switch (event.getClick()) {

            case LEFT: onLeftClick(event, menu); break;
            case RIGHT: onRightClick(event, menu); break;
            case SHIFT_LEFT: onShiftLeftClick(event, menu); break;
            case SHIFT_RIGHT: onShiftRightClick(event, menu); break;

        }

    }

    public abstract void onLeftClick(InventoryClickEvent event, AthenaMenu menu);
    public abstract void onRightClick(InventoryClickEvent event, AthenaMenu menu);
    public abstract void onShiftLeftClick(InventoryClickEvent event, AthenaMenu menu);
    public abstract void onShiftRightClick(InventoryClickEvent event, AthenaMenu menu);

}
