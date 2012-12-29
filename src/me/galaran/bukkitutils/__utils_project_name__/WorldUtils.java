package me.galaran.bukkitutils.__utils_project_name__;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldUtils {

    public static boolean isChunkLoadedAt(Location loc) {
        return loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }

    public static boolean isPlayerAround(final Location loc, int radius) {
        int radiusSquared = radius * radius;
        
        Location curPlayerLoc = loc.clone(); // reuse this
        for (Player player : Bukkit.getOnlinePlayers()) {
            curPlayerLoc = player.getLocation(curPlayerLoc);
            if (loc.getWorld().equals(curPlayerLoc.getWorld())) {
                if (loc.distanceSquared(curPlayerLoc) <= radiusSquared) {
                    return true;
                }
            }
        }
        return false;
    }
}
