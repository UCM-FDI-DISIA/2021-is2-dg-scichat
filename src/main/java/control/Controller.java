package control;

import control.options.Option;
import control.options.Option.ExecuteException;
import control.options.OptionGenerator;
import graphic.GameObserver;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import logic.Cell;
import logic.Game;
import logic.gameObjects.Player;
import org.json.JSONObject;
import org.json.JSONTokener;

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

    private int cnt = 0;

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
                    nextTurn();
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
        nextTurn();
    }

    public void reset() {
        game.reset();
    }

    public void softReset() {
        game.softReset();
    }

    private void nextTurn() {
        game.endTurn();
        game.advance();
        game.startTurn();
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
}
