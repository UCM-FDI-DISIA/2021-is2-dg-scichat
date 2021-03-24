package logic;

import exceptions.OccupiedCellException;
import logic.gameObjects.Player;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board = new Board();
    private boolean stopped = false;            /// Si el jugador ha parado el juego
    private List<Player> players = new ArrayList<>();

    public Game() {

    }

    public void addNewPlayer(Color color, Board.Side side) throws OccupiedCellException {
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

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(this.board.toString());

        result.append("List of players: \n");

        for (int i = 0; i < this.players.size(); ++i) {
            result.append(String.format("[%s]: %s - %s", i + 1, this.players.get(i).getColor(), this.players.get(i).getSide()));
            result.append("\n");
        }

        return result.toString();
    }
}
