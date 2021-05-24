package control.options;

import java.io.File;
import java.util.Scanner;
import logic.Game;

public class SaveGameOption extends Option {

    protected SaveGameOption(String title) {
        super(title);
    }

    public boolean execute(Game game, Scanner scanner) throws ExecuteException {
        System.out.println(
            "Ingrese el nombre del archivo donde desea guardar el juego: "
        );
        String fileName = scanner.next();

        File file = new File(fileName);

        game.saveGame(file);

        return true;
    }
}
