package control.commands;

import exceptions.OccupiedCellException;
import logic.Board;
import java.awt.Color;
import logic.Game;

public class AddPlayerCommand extends Command {
    /// Números correspondientes al enum
    private int colorInt;
    private int sideInt;

    protected AddPlayerCommand(String name, String shortcut, String details, String help) {
        super(name, shortcut, details, help);
    }

    @Override
    public boolean execute(Game game) throws ExecuteException {
        Color color = null;
        switch(colorInt) {
        case 0:
            color=Color.GREEN;
            break;
        case 1:
            color=Color.YELLOW;
            break;
        case 2:
            color=Color.ORANGE;
            break;
        case 3:
            color=Color.RED;
            break;
        case 4:
            color=Color.MAGENTA;
            break;
        case 5:
            color=Color.BLUE;
        }
        Board.Side side = Board.Side.values()[sideInt];

        try {
            game.addNewPlayer(color, side);
        } catch (OccupiedCellException e) {
            throw new ExecuteException("No se ha podido añadir al jugador");
        }

        return true;
    }

    @Override
    public Command parse(String[] commandWords) throws ParseException {
        if (!this.matchCommandName(commandWords[0])) return null;

        if (commandWords.length != 3) {
            //// La longitud de argumentos tiene que ser 2
            return null;
        }

        try {
            /// Parsing de los argumentos
            this.colorInt = Integer.parseInt(commandWords[1]);
            this.sideInt = Integer.parseInt(commandWords[2]);
        } catch (NumberFormatException nfe) {
            return null;
        }

        /// Solo hay 6 colores, numerados de 1 al 6
        if (!(this.colorInt >= 1 && this.colorInt <= 6)) {
            throw new ParseException("Invalid color Int");
        }

        /// Solo hay 6 lados, numerados de 1 al 6
        if (!(this.sideInt >= 1 && this.sideInt <= 6)) {
            throw new ParseException("Invalid side Int");
        }

        // Como serán usadas como índices, reducimos su valor por uno
        colorInt--;
        sideInt--;
        return this;
    }
}
