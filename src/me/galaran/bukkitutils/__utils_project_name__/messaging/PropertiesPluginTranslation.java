package me.galaran.bukkitutils.__utils_project_name__.messaging;

import org.bukkit.plugin.Plugin;

import java.io.*;
import java.util.Map;
import java.util.Properties;

public class PropertiesPluginTranslation extends Translation {

    private final Plugin plugin;

    public PropertiesPluginTranslation(Plugin plugin) {
        this.plugin = plugin;
    }

    public void reload(String language) throws IOException {
        String langFileName = language + ".lang";
        File langFile = new File(plugin.getDataFolder(), langFileName);
        if (!langFile.isFile()) {
            plugin.saveResource(langFileName, false);
            if (!langFile.isFile()) {
                throw new FileNotFoundException("No lang file for " + language);
            }
        }

        translation.clear();
        fillFromStream(new FileInputStream(langFile), translation);
    }

    public void addDefaults(InputStream is) throws IOException {
        fillFromStream(is, defaults);
    }

    private void fillFromStream(InputStream is, Map<String, String> map) throws IOException {
        Properties prop = new Properties();
        Reader reader = new InputStreamReader(is, "utf-8");
        prop.load(reader);
        reader.close();

        for (String curKey : prop.stringPropertyNames()) {
            map.put(curKey, prop.getProperty(curKey));
        }
    }
}
