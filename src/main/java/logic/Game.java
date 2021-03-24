package logic;

import logic.gameObjects.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board = new Board();
    private boolean stopped = false;            /// Si el jugador ha parado el juego
    private List<Player> players = new ArrayList<>();

    public Game() {

    }

    /**
     * Comprueba si el juego ha terminado o no
     *
     * @return si el juego sigue
     */
    public boolean isFinished() {
        return this.stopped;
    }

    @Override
    public String toString() {
        return this.board.toString();
    }
}
