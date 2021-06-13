package me.alex.smarthelp.utils;

import com.google.gson.internal.LinkedHashTreeMap;
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

    private final int similarityInt;
    private final int maxSuggestions;

    public ComponentUtils(int similarity, int maxSuggestions) {
        this.similarityInt = similarity;
        this.maxSuggestions = maxSuggestions;
    }

    public TextComponent getComponentMessage(HashMap<Integer, String> hashMap) {
        List<Template> plcLList = new ArrayList<>();
        AtomicInteger nearest = new AtomicInteger(1);
        LinkedHashTreeMap<Integer, String> linkedHashTreeMap = new LinkedHashTreeMap<>(Comparator.comparingInt(o -> o));
        linkedHashTreeMap.putAll(hashMap);
        TextComponent.Builder mainComponent = Component.text().append(MiniMessage.get().parse("<red>That Command does not Exists!</red> <hover:show_text:'<gray>Found <gold><commands></gold> Commands \n <blue>Hover over commands for more Information</blue>'><gradient:green:blue>(Help)</gradient> \n", Template.of("commands", hashMap.size() + "")));

        mainComponent.append(MiniMessage.get().parse("\n<gray>Did you mean any of these?</gray> "));

        if (linkedHashTreeMap.isEmpty())
            return mainComponent.append(MiniMessage.get().parse("\n<gray>- <red>We did not find any close Results</red>")).build();
        if (linkedHashTreeMap.keySet().stream().noneMatch(integer -> integer > similarityInt))
            return mainComponent.append(MiniMessage.get().parse("\n<gray>- <red>We found no similar Results</red>")).build();


        linkedHashTreeMap.entrySet().stream().limit(maxSuggestions).forEach(integerStringEntry -> {
            int close = integerStringEntry.getKey();
            String command = integerStringEntry.getValue();

            plcLList.clear();
            plcLList.add(Template.of("percentage", 100 - close + "%"));
            plcLList.add(Template.of("place", nearest.getAndIncrement() + ""));
            plcLList.add(Template.of("command", "/" + command));

            mainComponent.append(MiniMessage.get().parse("\n<gray>| <gold><bold><font:minecraft:uniform><place></font></bold> <click:suggest_command:<command>><hover:show_text:'<gray>Click here to insert the Command <gold><command></gold> <gray>(<green><percentage><gray>)</gray> '><gradient:#5e4fa2:#f79459:red><command></gradient></click>", plcLList));
        });


        return mainComponent.build();
    }
}
