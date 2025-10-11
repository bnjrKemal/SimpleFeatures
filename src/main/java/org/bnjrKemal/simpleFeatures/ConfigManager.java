package org.bnjrKemal.simpleFeatures;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManager {

    private static SimpleFeatures plugin;
    private static FileConfiguration config;
    private static File configFile;

    /**
     * Loads or creates the specified YAML file (e.g. messages.yml).
     */
    public static void setup(SimpleFeatures instance, String fileName) {
        plugin = instance;
        configFile = new File(plugin.getDataFolder(), fileName);

        if (!configFile.exists()) {
            plugin.saveResource(fileName, false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Returns any object from the YAML.
     */
    public static Object path(String path) {
        return config.get(path);
    }

    /**
     * Returns a formatted string from the YAML with & color codes and {prefix} support.
     */
    public static String getString(String path) {
        String value = config.getString(path);
        if (value == null)
            return "§c[Missing message: " + path + "]";
        String prefix = config.getString("prefix", "");
        return value.replace("&", "§")
                .replace("{prefix}", prefix.replace("&", "§"));
    }

    public static int getInt(String path) {
        return config.getInt(path);
    }

    public static boolean getBoolean(String path) {
        return config.getBoolean(path);
    }

    /**
     * Sets and saves a value in the YAML.
     */
    public static void set(String path, Object value) {
        config.set(path, value);
        save();
    }

    /**
     * Reloads the YAML file from disk.
     */
    public static void reload() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Saves the YAML file.
     */
    public static void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save " + configFile.getName());
        }
    }
}
