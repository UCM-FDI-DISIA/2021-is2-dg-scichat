package control.options;

import logic.Game;

public class MovePieceOption extends Option {
    protected MovePieceOption(String title) {
        super(title);
    }

    @Override
    public boolean execute(Game game) {
        return false;
    }
}
