package me.alex.smarthelp;

import me.alex.smarthelp.listeners.CommandProcessEvent;
import me.alex.smarthelp.utils.ComponentUtils;
import me.alex.smarthelp.utils.MathUtils;
import me.alex.smarthelp.utils.configuration.ConfigManager;
import me.alex.smarthelp.utils.configuration.ConfigValues;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

public final class SmartHelp extends JavaPlugin {

    private final MathUtils mathUtils = new MathUtils();
    private final ConfigManager configManager = new ConfigManager(this, "config.yml");
    private ComponentUtils componentUtils;
    private BukkitAudiences bukkitAudiences;
    private List<String> commands;

    @Override
    public void onEnable() {
        // Plugin startup logic
        createLoadAndSetConfig();
        this.bukkitAudiences = BukkitAudiences.create(this);

        this.componentUtils = new ComponentUtils(configManager);
        this.componentUtils.load();

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
                getLogger().info(ChatColor.GREEN + "Found %commands% Commands registered".replace("%commands%", commands.size() + ""));
            }
        }, 55);
    }

    private void createLoadAndSetConfig() {
        this.configManager.createFile();
        this.configManager.loadConfig();

        for (ConfigValues value : ConfigValues.values()) {
            this.configManager.saveDefaultValues(value);
        }

        this.configManager.saveFile();
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
