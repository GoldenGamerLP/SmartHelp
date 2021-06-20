package me.alex.smarthelp.commands;

import me.alex.smarthelp.SmartHelp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SmartHelpCommand implements CommandExecutor, TabCompleter {

    private final SmartHelp smartHelp;

    public SmartHelpCommand(SmartHelp smartHelp) {
        this.smartHelp = smartHelp;
        smartHelp.getCommand("shp").setExecutor(this);
        smartHelp.getCommand("shp").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            if (!player.hasPermission("smarthelp.use")) return false;
            if (args.length == 0) return false;
            sender.sendMessage(smartHelp.getCommands().toString());
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        return null;
    }
}
