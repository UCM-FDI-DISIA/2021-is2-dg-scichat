package control;

import control.options.Option;
import control.options.Option.ExecuteException;
import control.options.OptionGenerator;
import graphic.GameObserver;
import java.io.File;
import java.util.Scanner;
import javax.swing.*;
import logic.Cell;
import logic.Game;
import logic.gameObjects.HumanPlayer;
import logic.gameObjects.Player;

public class Controller {
    private Game game;
    private Scanner scanner;

    public Controller() {
        this.game = new Game();
    }

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

    public void addObserver(GameObserver in) {
        game.addObserver(in);
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
        game.currentPlayerSurrender();
        Player winner = game.wonBySurrender();
        if (winner != null) game.setStopped(true, winner);
        game.advance();
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
        this.game = game.loadGame(file);
    }

    public void saveGame(File file) {
        game.saveGame(file);
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
        if (!game.getCurrentPlayer().getId().equals(playerID)) return;

        this.surrender();
    }
}
