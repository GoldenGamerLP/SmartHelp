package me.alex.smarthelp.utils;

import me.alex.smarthelp.CMDSearchFinding;
import me.alex.smarthelp.utils.configuration.ConfigManager;
import me.alex.smarthelp.utils.configuration.ConfigValues;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.command.Command;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentUtils {

    private final ConfigManager configManager;
    private double similarityDouble;
    private int maxSuggestions;
    private String messageTitle;
    private String noResults;
    private String noSimilarResults;
    private String commandListingFormat;

    public ComponentUtils(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void load() {
        this.similarityDouble = (double) configManager.loadAndGet(ConfigValues.SIMILARITY_TRESHHOLD);
        this.maxSuggestions = (int) configManager.loadAndGet(ConfigValues.MAX_SUGGESTIONS);
        this.messageTitle = (String) configManager.loadAndGet(ConfigValues.UNKNOWNCOMMAND_TITLE);
        this.noResults = (String) configManager.loadAndGet(ConfigValues.UNKNOWNCOMMAND_NORESULTS);
        this.noSimilarResults = (String) configManager.loadAndGet(ConfigValues.UNKNOWNCOMMAND_NOSIMILARRESULTS);
        this.commandListingFormat = (String) configManager.loadAndGet(ConfigValues.COMMAND_LISTING);
    }

    public TextComponent getCommandList(TreeSet<CMDSearchFinding> searchFindings) {
        List<Template> plcLList = new ArrayList<>();
        AtomicInteger nearest = new AtomicInteger(1);

        TextComponent.Builder mainComponent = Component.text().append(MiniMessage.get().parse(messageTitle, Template.of("commands", searchFindings.size() + "")));
        //Bukkit.getServer().getLogger().info(searchFindings.stream().map(doubleCommandEntry -> doubleCommandEntry.getDouble() + " " + doubleCommandEntry.()).collect(Collectors.toList()).toString());

        if (searchFindings.isEmpty())
            return mainComponent.append(MiniMessage.get().parse(noResults)).build();
        if (searchFindings.stream().noneMatch(cmdSearchFinding -> cmdSearchFinding.getDouble() > similarityDouble))
            return mainComponent.append(MiniMessage.get().parse(noSimilarResults)).build();


        searchFindings.stream().limit(maxSuggestions).forEach(searchFinding -> {
            double close = searchFinding.getDouble();
            Command command = searchFinding.command();

            plcLList.clear();
            plcLList.add(Template.of("percentage", new DecimalFormat("##.#").format(close * 100) + "%"));
            plcLList.add(Template.of("place", nearest.getAndIncrement() + ""));
            plcLList.add(Template.of("command", "/" + command.getName()));

            mainComponent.append(MiniMessage.get().parse(commandListingFormat, plcLList));
        });


        return mainComponent.build();
    }
}
