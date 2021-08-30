package blue.athena.publicapi.bukkit.command;

import blue.athena.publicapi.AthenaPublicAPI;
import blue.athena.publicapi.misc.java.AthenaString;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class AthenaCommand extends Command {

    @Getter private static final HashMap<String, AthenaCommand> commandCache = new HashMap<>();

    @Getter private final CommandData data;

    @Getter @Setter private boolean enabled;
    @Getter private boolean hidden;

    @Getter private HashMap<String[], Method> outcomes;

    public AthenaCommand() {

        super("");

        data = this.getClass().getDeclaredAnnotation(CommandData.class);

        if (data == null) {

            AthenaString.consoleAnnounce("c", "&cProblem with AthenaCommand! &e@CommandData&c annotation is &8null&c.");
            return;

        }

        hidden = data.hidden();

        if (!hidden) {

            outcomes = new HashMap<>();
            registerOutcomes();

        }

    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] arguments) {

        if (!this.getName().equalsIgnoreCase(data.name())) return true;
        if (!data.enabled()) {

            tell(sender, data.checkEnabled());
            return true;

        }
        if (!data.console() && !(sender instanceof Player)) {

            tell(sender, data.checkConsole());
            return true;

        }

        if (arguments.length == 0) {

            onCommand(sender, label, arguments);
            return true;

        }

        for (String[] pattern : outcomes.keySet()) {

            if (check(pattern, arguments)) {

                Method m = outcomes.get(pattern);
                Outcome outcome = m.getDeclaredAnnotation(Outcome.class);

                if (outcome == null) continue;

                if (!outcome.enabled()) {

                    tell(sender, outcome.checkEnabled());
                    return true;

                }
                if (!(sender instanceof Player) && !outcome.console()) {

                    tell(sender, outcome.checkConsole());
                    return true;

                }
                if (outcome.perm().equalsIgnoreCase ("null") || (sender.isOp() && outcome.opBypass()) || sender.hasPermission(outcome.perm())) {

                    smartMethodExecution(m, label, arguments, sender);

                } else {

                    tell(sender, outcome.checkPermission());

                }

                return true;

            }

        }

        onCommand(sender, label, arguments);

        return true;

    }

    public abstract void onCommand(CommandSender sender, String label, String[] arguments);

    // Shortcuts

    private void smartMethodExecution(Method method, String label, String[] input, CommandSender sender) {

        List<Object> list = new ArrayList<>();

        for (Class<?> clazz : method.getParameterTypes()) {

            if (clazz == String.class) {

                list.add(label);

            } else if (clazz == String[].class) {

                list.add(input);

            } else if (clazz == CommandData.class) {

                list.add(data);

            }else if (clazz == Outcome.class) {

                list.add(method.getDeclaredAnnotation(Outcome.class));

            } else if (clazz == CommandSender.class) {

                list.add(sender);

            } else {

                list.add(null);

            }

        }

        try {

            method.setAccessible(true);
            method.invoke(this, list.toArray());
            method.setAccessible(false);

        } catch (InvocationTargetException | IllegalAccessException e) {

            AthenaString.console("&cCould not invoke subcommand method. Printing stacktrace.");
            e.getCause().printStackTrace();

        }

    }

    private HashMap<String[], Method> sort(HashMap<String[], Method> unsorted) {

        HashMap<String[], Method> sorted = new HashMap<>();

        int highest = 0;
        for (String[] ar : unsorted.keySet()) {

            if (ar.length > highest) highest = ar.length;

        }

        if (highest != 0) {

            for (int i = highest; i > 0; i--) {

                for (Map.Entry<String[], Method> entry : unsorted.entrySet()) {

                    if (entry.getKey().length == i) {

                        sorted.put(entry.getKey(), entry.getValue());

                    }

                }

            }

        }


        /*if (false) {

            for (Map.Entry<String[], Method> entry : unsorted.entrySet()) {

                AthenaString.console("KEY: " + Arrays.toString(entry.getKey()) + " | VALUE: " + entry.getValue().getName());

            }
            AthenaString.console("--------");
            for (Map.Entry<String[], Method> entry : sorted.entrySet()) {

                AthenaString.console("KEY: " + Arrays.toString(entry.getKey()) + " | VALUE: " + entry.getValue().getName());

            }

        }*/

        return sorted;

    }

    private boolean check(String[] pattern, String[] arguments) {

        if (pattern.length > arguments.length) return false;

        int length = pattern.length;
        boolean matches;

        for (int i = 0; i != length; i++) {

            matches = pattern[i].equalsIgnoreCase("*") || pattern[i].equalsIgnoreCase(arguments[i]);
            if (!matches) return false;

        }

        return true;

    }

    private void registerOutcomes() {

        Method[] methods = this.getClass().getDeclaredMethods();
        for (Method method : methods) {

            Outcome outcome = method.getDeclaredAnnotation(Outcome.class);
            if (outcome == null) continue;

            String pattern = outcome.pattern();
            String[] array = pattern.split("\\.");

            if (array.length != 0) outcomes.put(array, method);

        }

        outcomes = sort(outcomes);

    }

    public void registerCommand(CommandMap map) {

        this.setName(data.name());
        this.setDescription(AthenaString.addColor(data.desc()));
        this.setUsage(AthenaString.addColor(data.usage()));
        if (!data.perm().equalsIgnoreCase("null")) {

            this.setPermission(data.perm());
            this.setPermissionMessage(AthenaString.addColor(data.checkPermission()));

        }
        this.setAliases(Arrays.asList(data.aliases()));

        if (map.register(AthenaPublicAPI.getAPI().getPlugin().getName(), this)) {

            AthenaString.consoleAnnounce("a","&b&lAthena Public API &8» &aLoaded Command! (/" + data.name() + ")",
                    "&aFound " + outcomes.size() + " subcommand patterns while loading the command.");

        } else {

            AthenaString.consoleAnnounce("c","&b&lAthena Public API &8» &cFailed to load Command! (/" + data.name() + ")");

        }

    }

    public void tell(CommandSender sender, String... input) {

        for (String string : input) {

            sender.sendMessage(AthenaString.addColor(string));

        }

    }

}
