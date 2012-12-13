package me.galaran.bukkitutils.__utils_project_name__;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Messaging {

    private static Logger log;
    private static String chatPrefix;

    public static void init(Logger logger, String chatPrefixx) {
        log = logger;
        chatPrefix = ChatColor.GRAY + "[" + chatPrefixx + "] " + ChatColor.WHITE;
    }

    public static void log(Level level, String message, Object... params) {
        String finalString = StringUtils.parameterizeString(message, params);
        log.log(level, ChatColor.stripColor(finalString));
    }

    public static void log(String message, Object... params) {
        log(Level.INFO, message, params);
    }

    public static void send(CommandSender sender, String message, Object... params) {
        send(chatPrefix, sender, message, params);
    }

    public static void sendNoPrefix(CommandSender sender, String message, Object... params) {
        send("", sender, message, params);
    }

    private static void send(String prefix, CommandSender sender, String message, Object... params) {
        String decorated = StringUtils.decorateString(message, params);
        if (!decorated.equals("$suppress")) {
            sender.sendMessage(prefix + decorated);
        }
    }

    /** Does nothing, if player with specified name is not online */
    public static void send(String playerName, String message, Object... params) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player != null) {
            send(player, message, params);
        }
    }

    /** Does nothing, if player with specified name is not online */
    public static void sendNoPrefix(String playerName, String message, Object... params) {
        Player player = Bukkit.getPlayerExact(playerName);
        if (player != null) {
            sendNoPrefix(player, message, params);
        }
    }

    public static void broadcast(Location loc, double radius, String message, Object... params) {
        broadcast(chatPrefix, loc, radius, message, params);
    }

    public static void broadcastNoPrefix(Location loc, double radius, String message, Object... params) {
        broadcast("", loc, radius, message, params);
    }

    private static void broadcast(String prefix, Location loc, double radius, String message, Object... params) {
        for (Player curPlayer : Bukkit.getOnlinePlayers()) {
            Location curPlayerLoc = curPlayer.getLocation();
            if (!curPlayerLoc.getWorld().equals(loc.getWorld())) continue;
            if (curPlayerLoc.distance(loc) <= radius) {
                send(prefix, curPlayer, message, params);
            }
        }
    }

    public static void broadcastServerNoPrefix(String message, Object... params) {
        String decorated = StringUtils.decorateString(message, params);
        if (!decorated.equals("$suppress")) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "say " + decorated);
        }
    }

    public static String enDis(boolean state) {
        return state ? ChatColor.DARK_GREEN + "enabled" : ChatColor.DARK_RED + "disabled";
    }
}
