package blue.athena.publicapi.misc.java;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AthenaString {

    public static String addColor(String text) {

        return ChatColor.translateAlternateColorCodes('&', text);

    }
    public static void addColor(List<String> text) {

        for (int i = 0; i != text.size(); i++) {

            text.set(i, ChatColor.translateAlternateColorCodes('&', text.get(i)));

        }

    }

    public static String fullyProcess(String text, Player player) {

        String colored = addColor(text);
        return PlaceholderAPI.setPlaceholders(player, text);

    }

    public static void send(Player player, String... text) {

        for (String string : text) {

            player.sendMessage(addColor(string));

        }

    }
    public static void send(CommandSender sender, String... text) {

        for (String string : text) {

            sender.sendMessage(addColor(string));

        }

    }
    public static void console(String... text) {

        for (String string : text) {

            Bukkit.getConsoleSender().sendMessage(addColor(string));

        }

    }

    public static void consoleAnnounce(String colorCode, String... text) {

        String code = colorCode.replace("&", "");
        console("&8&m———&a&m———&8&m———&a&m———&8&m———".replace("a", code));
        console(" &r");
        console(text);
        console(" &r");
        console("&8&m———&a&m———&8&m———&a&m———&8&m———".replace("a", code));

    }

}
