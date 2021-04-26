package logic;

import control.options.Option.ExecuteException;
import exceptions.InvalidMoveException;
import exceptions.OccupiedCellException;
import graphic.GameObserver;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;
import org.apache.commons.lang.time.DurationFormatUtils;
import utils.Mode;
import utils.PieceColor;

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
    private ArrayList<GameObserver> observers = new ArrayList<>();

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
        for (GameObserver i : observers) {
            i.onGameEnded(this);
        }
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

    public void addObserver(GameObserver observer) {
        observer.onRegister(this);
        observers.add(observer);
    }

    /*Métodos*/

    public void addNewPlayer(PieceColor color, Board.Side side)
        throws OccupiedCellException {
        this.players.add(new Player(color, side, players.size() + 1));
    }

    /*Metodos de control de tiempo de juego*/

    /**
     * Empieza el temporizador del jugador actual
     */
    public void startTurn() {
        getCurrentPlayer().startTurn();
    }

    /**
     * Termina el temporizador del jugador actual
     */
    public void endTurn() {
        getCurrentPlayer().endTurn();
        for (GameObserver i : observers) {
            i.onEndTurn(this);
        }
    }

    /**
     *
     * @return	Tiempo en milisegundos que el jugador actual lleva jugando
     */
    public long getCurrentPlayerTime() {
        return this.getCurrentPlayer().timePlaying();
    }

    /**
     * Lleva a cabo instruccionees basicas para empezar una partida
     */
    public void start() {
	this.stopped=false;
        this.startTurn();
        for (GameObserver i : this.observers) i.onGameStart(this);
    }

    public Player currentPlayerSurrender() {
        this.getCurrentPlayer().surrender();
        for (GameObserver i : observers) {
            i.onSurrendered(this);
        }
        return this.wonBySurrender();
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
        do{
            this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
        }while(this.getCurrentPlayer().hasSurrender());
        for (GameObserver i : observers) {
            i.onEndTurn(this);
        }
    }

    /**
     * Obtener jugador del turno actual
     *
     * @return referencia al jugador del turno actual
     */
    public Player getCurrentPlayer() {
        return this.players.get(this.currentPlayerIndex);
    }

    public HashSet<Piece> getCurrentPlayerPieces() {
        return this.getCurrentPlayer().getPieces();
    }

    public boolean setSelectedPiece(Cell position) {
        boolean out = this.getCurrentPlayer().selectPiece(position.getPiece());
        if (out) {
            for (GameObserver i : observers) {
                i.onSelectedPiece(this.getCurrentPlayer().getSelectedPiece());
            }
        }
        return out;
    }

    public boolean hasSelectedPiece() {
        return this.getCurrentPlayer().hasSelectedPiece();
    }

    public void reset() {
        board = new Board();
        stopped = false;
        players = new ArrayList<>();
        observers = new ArrayList<>();
    }

    public void softReset() {
        //TODO preparar la partida para volver a empezar
    }

    public void movePiece(Cell to) throws ExecuteException {
        Piece selectedPiece = getCurrentPlayer().getSelectedPiece();
        if (selectedPiece == null) {
            throw new ExecuteException(String.format("No hay una pieza seleccionada"));
        }
        if (to == null) {
            throw new ExecuteException(
                String.format("No existe la celda a la que quieres mover la pieza \n")
            );
        }

        /// Intentar mover a la nueva celda
        /// Lanzaría una excepción si es movimiento inválido o celda ocupada
        try {
            selectedPiece.move(to, getGameMode());
        } catch (InvalidMoveException e) {
            throw new ExecuteException(
                String.format(
                    " Movimiento inválido a posición (%d, %d) \n",
                    to.getRow(),
                    to.getCol()
                )
            );
        }
    }

    //Para Debug
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
                    this.players.get(i).getColor().getName(),
                    this.players.get(i).getSide()
                )
            );
            result.append("\n");
        }

        result.append("\n");
        result.append(
            String.format("Turno del jugador: [%d] \n", this.currentPlayerIndex + 1)
        );
        result.append(
            "Tiempo de juego " +
            DurationFormatUtils.formatDuration(
                this.getCurrentPlayer().timePlaying(),
                "HH:mm:ss.S"
            ) +
            "\n"
        );
        return result.toString();
    }
}
