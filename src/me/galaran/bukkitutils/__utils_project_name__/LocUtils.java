package me.galaran.bukkitutils.__utils_project_name__;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class LocUtils {

    public static String toStringFull(Location loc) {
        return String.format(Locale.US, "%s, pitch: %.2f, yaw: %.2f", toStringWorldXYZ(loc), loc.getPitch(), loc.getYaw());
    }

    public static String toStringWorldXYZ(Location loc) {
        return loc.getWorld().getName() + ": " + toStringXYZ(loc);
    }

    public static String toStringXYZ(Location loc) {
        return String.format(Locale.US, "[%.2f %.2f %.2f]", loc.getX(), loc.getY(), loc.getZ());
    }

    public static boolean closerThan(Location loc1, Location loc2, double distance) {
        if (!loc1.getWorld().equals(loc2.getWorld())) {
            return false;
        } else {
            return loc1.distance(loc2) <= distance;
        }
    }

    public static Map<String, Object> serialize(Location loc) {
        if (loc == null) return null;

        Map<String, Object> locData = new LinkedHashMap<String, Object>();
        locData.put("x", loc.getX());
        locData.put("y", loc.getY());
        locData.put("z", loc.getZ());
        locData.put("world", loc.getWorld().getName());
        locData.put("pitch", loc.getPitch());
        locData.put("yaw", loc.getYaw());
        return locData;
    }
    
    public static Location deserialize(Map<String, Object> map) {
        String worldName = (String) map.get("world");
        World world = Bukkit.getServer().getWorld(worldName);
        if (world == null) {
            throw new IllegalArgumentException("Non-existent world: " + worldName);
        }

        Location result = new Location(world,
                ((Number) map.get("x")).doubleValue(),
                ((Number) map.get("y")).doubleValue(),
                ((Number) map.get("z")).doubleValue());
        
        if (map.containsKey("pitch")) {
            result.setPitch(((Number) map.get("pitch")).floatValue());
        }
        if (map.containsKey("yaw")) {
            result.setYaw(((Number) map.get("yaw")).floatValue());
        }
        
        return result;
    }

    public static Location deserialize(ConfigurationSection section) {
        return deserialize(section.getValues(false));
    }
}
