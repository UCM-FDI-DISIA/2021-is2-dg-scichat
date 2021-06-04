package control;

import exceptions.ExecuteException;
import graphic.GameObserver;
import java.io.File;
import javax.swing.*;
import logic.Cell;
import logic.Game;

public class Controller {
    private Game game;
    private LoadSaveCaretaker caretaker;

    public Controller() {
        this.game = new Game();
        this.caretaker = new LoadSaveCaretaker(game);
    }

    public Controller(Game game) {
        this.game = game;
        this.caretaker = new LoadSaveCaretaker(game);
    }

    public void setGame(Game newGame) {
        game = newGame;
        caretaker.setOriginator(game);
    }

    public void addObserver(GameObserver in) {
        game.addObserver(in);
    }

    public void handleClick(Cell position) {
        if (game.hasSelectedPiece()) {
            if (game.isSelectedPieceIn(position)) {
                game.deselectPiece();
            } else {
                try {
                    game.movePiece(position);
                    if (game.getCurrentPlayer().isAWinner()) game.setStopped(
                        true,
                        game.getCurrentPlayer()
                    );
                    game.advance();
                } catch (ExecuteException ex) {
                    showError(ex);
                }
            }
        } else {
            game.setSelectedPiece(position);
        }
    }

    public void surrender() {
        if (!game.isFinished()) {
            if (game.currentPlayerSurrender() == null) game.advance();
        }
    }

    public void reset() {
        game.reset();
    }

    public void softReset() {
        game.softReset();
    }

    public void initGame() {
        game.start();
    }

    public void showError(ExecuteException ex) {
        showError(ex, "Prueba de nuevo");
    }

    public void loadGame(File file) {
        this.game = caretaker.loadGame(file);
    }

    public void saveGame(File file) {
        caretaker.saveGame(file);
    }

    public void showError(ExecuteException ex, String optTxt) {
        String[] opt = { optTxt };
        JOptionPane.showOptionDialog(
            null,
            ex.getMessage(),
            "Execute Exception",
            JOptionPane.OK_OPTION,
            JOptionPane.ERROR_MESSAGE,
            null,
            opt,
            null
        );
    }

    public void onlineMovePiece(int x1, int y1, int x2, int y2, String playerID)
        throws ExecuteException {
        Cell from = this.game.getCell(x1, y1);
        Cell to = this.game.getCell(x2, y2);

        if (from.getPiece() == null) return;

        this.setCurrentPlayer(playerID);
        this.game.getCurrentPlayer().selectPiece(from.getPiece());
        this.game.movePiece(to);
        this.game.advance();
    }

    public void setCurrentPlayer(String playerID) {
        while (!this.game.getCurrentPlayer().getId().equals(playerID)) {
            this.game.advance();
        }
    }

    public void surrender(String playerID) {
        if (!game.isFinished()) {
            game.playerSurrender(playerID);
        }
    }
}
