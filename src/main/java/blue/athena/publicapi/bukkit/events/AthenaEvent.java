package blue.athena.publicapi.bukkit.events;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AthenaEvent extends Event {

    public void call() {

        Bukkit.getPluginManager().callEvent(this);

    }

    @Getter private static final HandlerList handlerList = new HandlerList();

    @Override
    public HandlerList getHandlers() {

        return handlerList;

    }

}
