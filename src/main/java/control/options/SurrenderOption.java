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
	Player currentPlayer = game.getCurrentPlayer();
	currentPlayer.surrender();
	//Player p = game.wonBySurrender();
	currentPlayer = game.wonBySurrender();
	if (currentPlayer != null) {
	    game.setStopped(true);
	}
	game.deleteCurrentPlayer();
	return true;
    }
}
