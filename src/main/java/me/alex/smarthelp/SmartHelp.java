package me.alex.smarthelp;

import me.alex.smarthelp.listeners.CommandProcessEvent;
import me.alex.smarthelp.utils.ComponentUtils;
import me.alex.smarthelp.utils.MathUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public final class SmartHelp extends JavaPlugin {


    private final File file = new File(this.getDataFolder().getAbsolutePath() + "//configuration.yml");
    private final MathUtils mathUtils = new MathUtils();
    private YamlConfiguration yamlConfiguration;
    private ComponentUtils componentUtils;
    private int maxsuggestions;
    private int similarity;
    private BukkitAudiences bukkitAudiences;
    private List<String> commands;

    @Override
    public void onEnable() {
        // Plugin startup logic
        createLoadAndSetConfig();
        this.bukkitAudiences = BukkitAudiences.create(this);

        this.componentUtils = new ComponentUtils(similarity, maxsuggestions);
        this.commands = new SimpleCommandMap(this.getServer()).getCommands().stream().map(Command::getName).collect(Collectors.toList());
        new CommandProcessEvent(this);
        getAllCommands();
    }

    private void getAllCommands() {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(this, () -> {
            try {
                final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                bukkitCommandMap.setAccessible(true);
                SimpleCommandMap commandMap = (SimpleCommandMap) bukkitCommandMap.get(Bukkit.getServer());

                // do stuff with commandMap
                commands = commandMap.getCommands().stream().map(command -> command.getLabel().trim()).filter(s -> !s.isEmpty()).distinct().collect(Collectors.toList());
            } catch (NoSuchFieldException | IllegalAccessException e) {
                getLogger().info(e.fillInStackTrace().getLocalizedMessage());
            } finally {
                getLogger().info("Found %commands% Commands registered".replace("%commands%", commands.size() + ""));
            }
        }, 55);
    }

    private void createLoadAndSetConfig() {
        if (!file.exists()) {
            try {
                file.mkdir();
                file.createNewFile();
            } catch (IOException e) {
                this.getLogger().info(ChatColor.LIGHT_PURPLE + e.fillInStackTrace().getLocalizedMessage() + String.format("[%s]", this.getDataFolder().getAbsolutePath()));
            } finally {
                this.yamlConfiguration = YamlConfiguration.loadConfiguration(file);
                yamlConfiguration.set("smarthelp.values.similarity", 3);
                yamlConfiguration.set("smarthelp.values.maxsuggestions", 5);
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
    }

    @Override
    public void onDisable() {
        if (this.bukkitAudiences != null) {
            bukkitAudiences.close();
            bukkitAudiences = null;
        }
    }

    public MathUtils getMathUtils() {
        return mathUtils;
    }

    public ComponentUtils getComponentUtils() {
        return componentUtils;
    }

    public BukkitAudiences getBukkitAudiences() {
        if (this.bukkitAudiences == null) {
            throw new IllegalStateException("Tried to access Adventure when the plugin was disabled!");
        }
        return this.bukkitAudiences;
    }

    public List<String> getCommands() {
        return commands;
    }
}
