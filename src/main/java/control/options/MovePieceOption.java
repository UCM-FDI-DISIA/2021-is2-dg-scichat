package control.options;

import logic.Game;

import java.util.Scanner;

public class MovePieceOption extends Option {
    protected MovePieceOption(String title) {
        super(title);
    }

    @Override
    public boolean execute(Game game, Scanner scanner) {

        return false;
    }
}
