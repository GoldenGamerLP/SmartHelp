package me.alex.smarthelp.utils.configuration;

public enum ConfigValues {

    MAX_SUGGESTIONS("smarthelp.maxsuggestions", 3),
    SIMILARITY_TRESHHOLD("smarthelp.similaritythreashhold", 0.2),
    UNKNOWNCOMMAND_TITLE("smarthelp.messages.unknowncommand.title", "<red>That Command does not Exists!</red> <hover:show_text:'<gray>Found <gold><commands></gold> Commands \n <blue>Hover over commands for more Information</blue>'><gradient:green:blue>(Help)</gradient> \n \n <gray>Did you mean any of these?</gray>"),
    UNKNOWNCOMMAND_NOSIMILARRESULTS("smarthelp.messages.unknowncommand.nosimilarresults", "\n<gray>- <red>We found no similar Results</red>"),
    UNKNOWNCOMMAND_NORESULTS("smarthelp.messages.unknowncommand.noresults", "\n<gray>- <red>We found no results</red>"),
    COMMAND_LISTING("smarthelp.messages.commandListingFormat", "\n <gray>| <place> <click:suggest_command:<command>><hover:show_text:'<gray>Click here to insert the Command <gold><command></gold> <gray>(<green><percentage><gray>)</gray>'><gradient:BLUE:AQUA><command></gradient></click>");

    private final String configPath;
    private final Object value;

    ConfigValues(String configPath, Object value) {
        this.configPath = configPath;
        this.value = value;
    }

    public String getConfigPath() {
        return configPath;
    }

    public Object getValue() {
        return value;
    }
}
