package network.commands;

public abstract class CommandParser {

    public static class ParseException extends RuntimeException {

        public ParseException(String _type) {
            super(String.format("ERROR: %s no es un tipo de comando soportado", _type));
        }
    }

    public abstract Command[] getCommands();

    public Command parse(String _type) throws ParseException {
        for (Command c : this.getCommands()) {
            if (c.parse(_type) != null) return c.parse(_type);
        }

        throw new ParseException(_type);
    }
}