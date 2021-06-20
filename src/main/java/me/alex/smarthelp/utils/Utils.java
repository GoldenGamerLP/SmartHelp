package me.alex.smarthelp.utils;

import me.alex.smarthelp.SmartHelp;
import org.bukkit.Bukkit;
import org.bukkit.help.HelpTopic;

public class Utils {

    private final SmartHelp smartHelp;

    public Utils(SmartHelp smartHelp) {
        this.smartHelp = smartHelp;
    }

    public boolean commandExits(String command) {
        return Bukkit.getHelpMap().getHelpTopic(command) == null;
    }

    public HelpTopic getCommandInfo(String command) {
        return Bukkit.getHelpMap().getHelpTopic(command.toLowerCase());
    }

}
