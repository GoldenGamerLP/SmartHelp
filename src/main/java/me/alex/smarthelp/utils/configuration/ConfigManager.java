package me.alex.smarthelp.utils.configuration;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private final Plugin plugin;
    private final File file;
    private final Map<String, Object> objectClassMap = new HashMap<>();
    private YamlConfiguration yamlConfiguration;

    public ConfigManager(Plugin plugin, String fileName) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder().getAbsolutePath() + "//" + fileName);
    }

    public void createFile() {
        if (this.file.exists()) {
            try {
                this.file.mkdir();
                this.file.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().info(ChatColor.RED + e.fillInStackTrace().getLocalizedMessage());
            }
        }
    }

    public void loadConfig() {
        this.yamlConfiguration = YamlConfiguration.loadConfiguration(this.file);
    }

    public void reload() {
        this.objectClassMap.clear();
    }

    public Object loadAndGet(ConfigValues values) {
        if (!this.objectClassMap.containsKey(values.getConfigPath()))
            this.objectClassMap.put(values.getConfigPath(), this.yamlConfiguration.get(values.getConfigPath()));
        return this.objectClassMap.get(values.getConfigPath());
    }

    public void saveDefaultValues(ConfigValues values) {
        if (this.yamlConfiguration.get(values.getConfigPath()) == null)
            this.yamlConfiguration.set(values.getConfigPath(), values.getValue());
    }

    public void saveFile() {
        try {
            this.yamlConfiguration.save(this.file);
        } catch (IOException e) {
            plugin.getLogger().info(ChatColor.RED + e.fillInStackTrace().getLocalizedMessage());
        }
    }

}
