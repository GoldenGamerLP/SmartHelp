package me.alex.smarthelp;

import me.alex.smarthelp.listeners.CommandProcessEvent;
import me.alex.smarthelp.utils.ComponentUtils;
import me.alex.smarthelp.utils.MathUtils;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class SmartHelp extends JavaPlugin {


    private final File file = new File(this.getDataFolder().getAbsolutePath() + "//configuration.yml");
    private YamlConfiguration yamlConfiguration;
    private final MathUtils mathUtils = new MathUtils();
    private ComponentUtils componentUtils;

    @Override
    public void onEnable() {
        // Plugin startup logic
        int maxsuggestions;
        int similarity;

        if(!file.exists()) {
            try {
                file.mkdir();
                file.createNewFile();
            } catch (IOException e) {
                this.getLogger().info(ChatColor.LIGHT_PURPLE + e.fillInStackTrace().getLocalizedMessage() + String.format("[%s]", this.getDataFolder().getAbsolutePath()));
            } finally {
                this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
               yamlConfiguration.set("smarthelp.values.similarity",3);
               yamlConfiguration.set("smarthelp.values.maxsuggestions",5);
               this.getLogger().info("Config was loaded!");

                try {
                    yamlConfiguration.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    maxsuggestions = yamlConfiguration.getInt("smarthelp.values.maxsuggestions");
                    similarity = yamlConfiguration.getInt("smarthelp.values.similarity");
                    this.getLogger().info(String.format("Config was redden and saved! [%o,%o]", maxsuggestions, similarity));
                }
            }
        } else {
            this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
            maxsuggestions = yamlConfiguration.getInt("smarthelp.values.maxsuggestions");
            similarity = yamlConfiguration.getInt("smarthelp.values.similarity");
            this.getLogger().info(String.format("Config was redden and saved! [%o,%o]", maxsuggestions, similarity));
        }

        this.componentUtils = new ComponentUtils(similarity, maxsuggestions);
        new CommandProcessEvent(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public MathUtils getMathUtils() {
        return mathUtils;
    }

    public ComponentUtils getComponentUtils() {
        return componentUtils;
    }
}
