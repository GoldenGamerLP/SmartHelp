package me.alex.smarthelp.utils;

import com.google.gson.internal.LinkedHashTreeMap;
import me.alex.smarthelp.utils.configuration.ConfigManager;
import me.alex.smarthelp.utils.configuration.ConfigValues;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ComponentUtils {

    private final ConfigManager configManager;
    private int similarityInt;
    private int maxSuggestions;
    private String messageTitle;
    private String noResults;
    private String noSimilarResults;
    private String commandListingFormat;

    public ComponentUtils(ConfigManager configManager) {
        this.configManager = configManager;
    }

    public void load() {
        this.similarityInt = (int) configManager.loadAndGet(ConfigValues.SIMILARITY_TRESHHOLD);
        this.maxSuggestions = (int) configManager.loadAndGet(ConfigValues.MAX_SUGGESTIONS);
        this.messageTitle = (String) configManager.loadAndGet(ConfigValues.UNKNOWNCOMMAND_TITLE);
        this.noResults = (String) configManager.loadAndGet(ConfigValues.UNKNOWNCOMMAND_NORESULTS);
        this.noSimilarResults = (String) configManager.loadAndGet(ConfigValues.UNKNOWNCOMMAND_NOSIMILARRESULTS);
        this.commandListingFormat = (String) configManager.loadAndGet(ConfigValues.COMMAND_LISTING);
    }

    public TextComponent getComponentMessage(HashMap<Integer, String> hashMap) {
        List<Template> plcLList = new ArrayList<>();
        AtomicInteger nearest = new AtomicInteger(1);
        LinkedHashTreeMap<Integer, String> linkedHashTreeMap = new LinkedHashTreeMap<>(Comparator.comparingInt(o -> o));
        linkedHashTreeMap.putAll(hashMap);
        TextComponent.Builder mainComponent = Component.text().append(MiniMessage.get().parse(messageTitle, Template.of("commands", hashMap.size() + "")));

        if (linkedHashTreeMap.isEmpty())
            return mainComponent.append(MiniMessage.get().parse(noResults)).build();
        if (linkedHashTreeMap.keySet().stream().noneMatch(integer -> integer > similarityInt))
            return mainComponent.append(MiniMessage.get().parse(noSimilarResults)).build();


        linkedHashTreeMap.entrySet().stream().limit(maxSuggestions).forEach(integerStringEntry -> {
            int close = integerStringEntry.getKey();
            String command = integerStringEntry.getValue();

            plcLList.clear();
            plcLList.add(Template.of("percentage", 100 - close + "%"));
            plcLList.add(Template.of("place", nearest.getAndIncrement() + ""));
            plcLList.add(Template.of("command", "/" + command));

            mainComponent.append(MiniMessage.get().parse(commandListingFormat, plcLList));
        });


        return mainComponent.build();
    }
}
