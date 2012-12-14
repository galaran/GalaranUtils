package me.galaran.bukkitutils.__utils_project_name__.messaging;

import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class Translation {

    protected final Map<String, String> translation = new HashMap<String, String>();
    protected final Map<String, String> defaults = new HashMap<String, String>();

    public String getString(String key) {
        String result = translation.get(key);
        if (result == null) {
            result = defaults.get(key);
            if (result == null) {
                result = ChatColor.RED + "Missing translation for key " + ChatColor.DARK_RED + key;
                Messaging.log(Level.WARNING, result);
            }
        }
        return result;
    }

    public void addDefaults(String... keyValues) {
        if (keyValues.length < 2 || keyValues.length % 2 != 0) {
            throw new IllegalArgumentException("Defauls must be key-value pairs");
        }

        for (int i = 0; i < keyValues.length; i += 2) {
            defaults.put(keyValues[i], keyValues[i + 1]);
        }
    }
}
