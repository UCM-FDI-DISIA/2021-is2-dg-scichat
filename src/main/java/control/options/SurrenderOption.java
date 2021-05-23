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
        Player victoryPlayer = game.currentPlayerSurrender();
        if (victoryPlayer != null) {
            game.setWinner(victoryPlayer);
            game.setStopped(true);
            System.out.println("Ha ganado el jugador " + victoryPlayer.getId() + "\n");
            out = false;
        }
        return out;
    }
}
