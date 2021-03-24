package control.commands;

public class CommandGenerator {
    private static Command[] availableCommands = {
            new AddPlayerCommand("addPlayer", "ap", "[a]dd[P]layer <color> <side>", "add player with color and side")
    };

    public static Command parse(String[] parameters) throws Command.ParseException {
        for (int i = 0; i < CommandGenerator.availableCommands.length; ++i) {
            Command parsed = CommandGenerator.availableCommands[i].parse(parameters);
            if (parsed != null) return parsed;
        }

        throw new Command.ParseException("Unknown command");
    }

    public static String commandHelp() {
        StringBuilder output = new StringBuilder();

        for (Command command : CommandGenerator.availableCommands) {
            output.append(command.helpText());
        }

        return output.toString();
    }
}
