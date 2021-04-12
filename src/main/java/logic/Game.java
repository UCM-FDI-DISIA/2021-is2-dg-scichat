package logic;

import exceptions.OccupiedCellException;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import logic.gameObjects.Player;
import utils.Mode;
import utils.Util;

public class Game implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Board board = new Board();
    private boolean stopped = false; /// Si el jugador ha parado el juego
    private ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private Mode gameMode;

    public Game() {}

    /*Getters*/

    public Board getBoard() {
        return board;
    }

    public Cell getCell(int row, int col) {
        return this.board.getCell(row, col);
    }

    public boolean getStopped() {
        return stopped;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    
    public Mode getGameMode() {
	return gameMode;
    }

    /*Setters*/

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }
    
    public void setGameMode(Mode modo) {
   	gameMode = modo;
   }

    /*Métodos*/

    public void addNewPlayer(Color color, Board.Side side)
        throws OccupiedCellException {
        this.players.add(new Player(color, side));
    }

    /**
     * Comprueba si el juego ha terminado o no
     *
     * @return si el juego sigue
     */
    public boolean isFinished() {
        return this.stopped;
    }

    /**
     * Comprueba si hay un ganador y lo devuelve si hay, si no devuelve null
     *
     * @return null si no hay ganador por rendicion, si lo hay devuelve el ganador
     */
    public Player wonBySurrender() {
        Player out = null;
        for (Player i : players) {
            if (!i.hasSurrender()) {
                if (out != null) return null; else out = i;
            }
        }
        return out;
    }

    /**
     * Avanzar en turno
     */
    public void advance() {
        this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
    }

    /**
     * Obtener jugador del turno actual
     *
     * @return referencia al jugador del turno actual
     */
    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayerIndex);
    }

    public void deleteCurrentPlayer() {
        players.remove(this.currentPlayerIndex);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(this.board.toString());

        result.append("List of players: \n\n");

        for (int i = 0; i < this.players.size(); ++i) {
            result.append(
                String.format(
                    "   [%s]: %s - %s",
                    i + 1,
                    Util.col2str(this.players.get(i).getColor()),
                    this.players.get(i).getSide()
                )
            );
            result.append("\n");
        }

        result.append("\n");
        result.append(
            String.format("Turno del jugador: [%d] \n", this.currentPlayerIndex + 1)
        );

        return result.toString();
    }
}
