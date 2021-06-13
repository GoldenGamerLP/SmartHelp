package me.alex.smarthelp.listeners;

import me.alex.smarthelp.SmartHelp;
import me.alex.smarthelp.utils.ComponentUtils;
import me.alex.smarthelp.utils.MathUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

public class CommandProcessEvent implements Listener {

    private final MathUtils mathUtils;
    private final ComponentUtils componentUtils;

    public CommandProcessEvent(SmartHelp smartHelp) {
        this.componentUtils = smartHelp.getComponentUtils();
        this.mathUtils = smartHelp.getMathUtils();
        smartHelp.getServer().getPluginManager().registerEvents(this, smartHelp);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().isEmpty()) return;
        String command = event.getMessage().split(" ")[0].toLowerCase();
        HelpTopic helpTopic = Bukkit.getServer().getHelpMap().getHelpTopic(command);
        if (helpTopic == null) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(componentUtils.getComponentMessage(mathUtils.getBestResult(command, Bukkit.getCommandMap().getKnownCommands().keySet())));
        }
    }

}
