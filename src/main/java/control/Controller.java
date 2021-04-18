package control;

import control.options.Option;
import control.options.OptionGenerator;
import java.util.Scanner;
import logic.Game;

public class Controller {
    private Game game;
    private Scanner scanner;

    public Controller(Game game, Scanner scanner) {
        this.game = game;
        this.scanner = scanner;
    }

    public void printGame() {
        System.out.println(this.game);
    }

    public void setGame(Game newGame) {
        game = newGame;
    }

    public void run() {
        boolean refreshDisplay = true;

        while (!game.isFinished()) {
            game.startTurn();
            if (refreshDisplay) printGame();
            refreshDisplay = false;

            OptionGenerator.printOptions();

            try {
                Option option = OptionGenerator.parse(scanner);
                System.out.format(
                    "[DEBUG]: Se ha seleccionado la opción: [%s] \n\n",
                    option.title
                );
                refreshDisplay = option.execute(game, scanner);
                game.endTurn();
                if (refreshDisplay) game.advance();
            } catch (Exception ex) {
                System.out.format(ex.getMessage() + "%n%n");
            }
        }

        if (refreshDisplay) printGame();
        System.out.println("[GAME OVER]");
    }
}
