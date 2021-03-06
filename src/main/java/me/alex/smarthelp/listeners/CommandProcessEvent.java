package me.alex.smarthelp.listeners;

import me.alex.smarthelp.SmartHelp;
import me.alex.smarthelp.utils.ComponentUtils;
import me.alex.smarthelp.utils.MathUtils;
import me.alex.smarthelp.utils.Utils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandProcessEvent implements Listener {

    private final MathUtils mathUtils;
    private final ComponentUtils componentUtils;
    private final BukkitAudiences bukkitAudiences;
    private final SmartHelp smartHelp;
    private final Utils utils;

    public CommandProcessEvent(SmartHelp smartHelp) {
        this.bukkitAudiences = smartHelp.getBukkitAudiences();
        this.componentUtils = smartHelp.getComponentUtils();
        this.mathUtils = smartHelp.getMathUtils();
        this.smartHelp = smartHelp;
        this.utils = smartHelp.getUtils();
        smartHelp.getServer().getPluginManager().registerEvents(this, smartHelp);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().isEmpty()) return;
        if (smartHelp.getCommands() == null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§7[§aSmartHelp§7] Please wait - plugin isn't yet ready!");
            return;
        }


        String command = event.getMessage().split(" ")[0].toLowerCase();
        if (utils.commandExits(command)) {
            event.setCancelled(true);
            bukkitAudiences.player(event.getPlayer()).sendMessage(componentUtils.getCommandList(mathUtils.getBestResult(command, smartHelp.getCommands())));
        }
    }

}
