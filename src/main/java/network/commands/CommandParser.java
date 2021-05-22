package network.commands;

public abstract class CommandParser {

    /**
     * Como en las distintas ventanas, el conjunto de comandos soportados es diferente,
     * para usar CommandParser, hay que sobreescribir este método devolviendo el array de comandos soportados
     *
     * @return array de comandos soportados
     */
    public abstract Command[] getCommands();

    public final Command parse(String _type) throws IllegalArgumentException {
        for (Command c : this.getCommands()) {
            if (c.parseCommand(_type) != null) return c.parseCommand(_type);
        }

        throw new IllegalArgumentException(
            String.format("ERROR: %s no es un tipo de comando soportado", _type)
        );
    }
}
