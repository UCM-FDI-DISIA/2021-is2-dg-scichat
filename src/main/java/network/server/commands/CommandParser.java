package network.server.commands;

public class CommandParser {
    private static final Command[] availableCommands = {};

    public static class ParseException extends RuntimeException {

        public ParseException(String _type) {
            super(String.format("ERROR: %s no es un tipo de comando soportado", _type));
        }
    }

    public static Command parse(String _type) throws ParseException {
        for (Command c : availableCommands) {
            if (c.parse(_type) != null) return c.parse(_type);
        }

        throw new ParseException(_type);
    }
}
