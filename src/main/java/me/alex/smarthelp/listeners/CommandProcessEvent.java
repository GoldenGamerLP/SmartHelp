package me.alex.smarthelp.listeners;

import me.alex.smarthelp.SmartHelp;
import me.alex.smarthelp.utils.ComponentUtils;
import me.alex.smarthelp.utils.MathUtils;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.help.HelpTopic;

public class CommandProcessEvent implements Listener {

    private final MathUtils mathUtils;
    private final ComponentUtils componentUtils;
    private final BukkitAudiences bukkitAudiences;
    private final SmartHelp smartHelp;

    public CommandProcessEvent(SmartHelp smartHelp) {
        this.bukkitAudiences = smartHelp.getBukkitAudiences();
        this.componentUtils = smartHelp.getComponentUtils();
        this.mathUtils = smartHelp.getMathUtils();
        this.smartHelp = smartHelp;
        smartHelp.getServer().getPluginManager().registerEvents(this, smartHelp);
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (event.isCancelled()) return;
        if (event.getMessage().isEmpty()) return;

        String command = event.getMessage().split(" ")[0].toLowerCase();
        HelpTopic helpTopic = Bukkit.getServer().getHelpMap().getHelpTopic(command);
        if (helpTopic == null) {
            event.setCancelled(true);
            bukkitAudiences.player(event.getPlayer()).sendMessage(componentUtils.getComponentMessage(mathUtils.getBestResult(command, smartHelp.getCommands())));
        }
    }

}
