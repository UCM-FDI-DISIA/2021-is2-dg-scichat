package control.options;

import java.util.Scanner;
import logic.Game;
import logic.gameObjects.Player;

public class SurrenderOption extends Option {

    protected SurrenderOption(String title) {
        super(title);
    }

    @Override
    public boolean execute(Game game, Scanner scanner) throws ExecuteException {
        boolean out = true;
        Player currentPlayer = game.currentPlayerSurrender();
        if (currentPlayer != null) {
            game.setStopped(true);
            System.out.println("Ha ganado el jugador " + currentPlayer.getId() + "\n");
            out = false;
        }
        return out;
    }
}
