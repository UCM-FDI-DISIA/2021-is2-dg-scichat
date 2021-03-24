package control.commands;

public abstract class Command {
    protected final String name;
    protected final String shortcut;
    private final String details;
    private final String help;

    protected static final String incorrectNumberOfArgsMsg = "Incorrect number of arguments";
    protected static final String incorrectArgsMsg = "Incorrect arguments format";

    /// Lo dejo de momento como una clase anidad, porque al final, estas excepciones solamente se lanza en Command
    public static class ParseException extends Exception {
        public ParseException(String message) {
            super("[ERROR]: " + message);
        }
    }

    public static class ExecuteException extends Exception {
        public ExecuteException(String message) {
            super("[ERROR]: " + message);
        }
    }

    protected Command(String name, String shortcut, String details, String help) {
        this.name = name;
        this.shortcut = shortcut;
        this.details = details;
        this.help = help;
    }

    /// Pendiente por decidir qué parámetro pasar, probablemente la referencia a la clase Game
    public abstract boolean execute() throws ExecuteException;

    public abstract Command parse(String[] commandWords) throws ParseException;

    protected boolean matchCommandName(String name) {
        return this.shortcut.equalsIgnoreCase(name) ||
                this.name.equalsIgnoreCase(name);
    }

    protected Command parseNoParamsCommand(String[] words) throws ParseException {
        if (matchCommandName(words[0])) {
            if (words.length != 1)
                throw new ParseException("Command " + name + " :" + incorrectNumberOfArgsMsg);
            else return this;
        }
        return null;
    }

    public String helpText() {
        return details + ": " + help + "\n";
    }
}
