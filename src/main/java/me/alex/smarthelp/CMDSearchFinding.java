package me.alex.smarthelp;

import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;

public record CMDSearchFinding(Double close, Command command) implements Comparable<CMDSearchFinding> {

    public double getDouble() {
        return close;
    }

    public Command command() {
        return command;
    }


    @Override
    public int compareTo(@NotNull CMDSearchFinding o) {
        return 0;
    }
}
