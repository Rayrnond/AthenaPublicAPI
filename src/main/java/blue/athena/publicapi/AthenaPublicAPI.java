package blue.athena.publicapi;

import blue.athena.publicapi.bukkit.command.AthenaCommand;
import blue.athena.publicapi.bukkit.command.CommandData;
import blue.athena.publicapi.bukkit.events.AthenaListener;
import blue.athena.publicapi.misc.java.AthenaString;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collection;

public final class AthenaPublicAPI {

    @Getter private static AthenaPublicAPI API;

    @Getter private final JavaPlugin plugin;

    public AthenaPublicAPI(JavaPlugin plugin) {

        API = this;
        this.plugin = plugin;

        register();

    }

    private void register()  {

        ClassGraph graph = new ClassGraph();
        graph.enableAllInfo();

        String[] group = plugin.getClass().getPackage().getName().split("\\.");

        graph.acceptPackages(group[0] + "." + group[1]);
        graph.enableClassInfo();

        ScanResult result = graph.scan();

        Collection<Class<?>> commandClasses = result.getClassesWithAnnotation(CommandData.class.getName()).loadClasses();

        try {

            final Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            if (map == null) throw new NullPointerException("Bukkit CommandMap is null.");

            int total = 0;
            for (Class<?> clazz : commandClasses) {

                try {

                    if (clazz == AthenaCommand.class) {

                        Object instance = clazz.newInstance();
                        AthenaCommand command = (AthenaCommand) instance;

                        if (command.isHidden()) continue;

                        command.registerCommand(map);
                        total++;

                    } else {

                        AthenaString.consoleAnnounce("c", "&b&lAthena Public API &8» &e@CommandData &cwas placed on non-AthenaCommand class.");

                    }

                } catch (InstantiationException | IllegalAccessException e) {

                    AthenaString.consoleAnnounce("c","&b&lAthena Public API &8» &cFailed to load AthenaCommand! (" + clazz.getName()+ ")");
                    e.printStackTrace();

                }

            }

            field.setAccessible(false);

            AthenaString.consoleAnnounce("a", "&b&lAthena Public API &8» &aLoaded " + total + " AthenaCommands.");

        } catch (NoSuchFieldException | IllegalAccessException | NullPointerException e) {

            AthenaString.console("&4Could not get Bukkit CommandMap (probably a version issue!)");
            e.printStackTrace();

        }

        Collection<Class<?>> listenerClasses = result.getClassesWithAnnotation(AthenaListener.class.getName()).loadClasses();

        int listeners = 0;
        for (Class<?> clazz : listenerClasses) {

            if (clazz.isAssignableFrom(Listener.class)) {

                try {
                    Bukkit.getPluginManager().registerEvents((Listener) clazz.newInstance(), getPlugin());
                    listeners++;
                } catch (InstantiationException | IllegalAccessException e) {

                    AthenaString.console("&cFailed registering &e@AthenaListener&c class. Printing stacktrace.");
                    e.printStackTrace();

                }

            }

        }
        AthenaString.consoleAnnounce("a", "&b&lAthena Public API &8» &aLoaded " + listeners +"  AthenaListeners!");

    }

}
