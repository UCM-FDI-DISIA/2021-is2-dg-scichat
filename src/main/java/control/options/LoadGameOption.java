package control.options;

import java.io.File;
import java.util.Scanner;
import logic.Game;

public class LoadGameOption extends Option {

    protected LoadGameOption(String title) {
        super(title);
    }

    public boolean execute(Game game, Scanner scanner) throws ExecuteException {
        System.out.println(
            "Ingrese el nombre del archivo desde donde desea cargar el juego: "
        );

        String fileName = scanner.next();

        File file = new File(fileName);

        game.loadGame(file);

        return true;
    }
}
