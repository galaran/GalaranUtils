package me.galaran.bukkitutils.__utils_project_name__;

import org.bukkit.Location;

public class WorldUtils {

    public static boolean isChunkLoadedAt(Location loc) {
        return loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }
}
