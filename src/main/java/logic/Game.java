package logic;

import control.options.Option.ExecuteException;
import exceptions.InvalidMoveException;
import exceptions.OccupiedCellException;
import graphic.GameObserver;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
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
    private Player winner = null;
    private ArrayList<GameObserver> observers = new ArrayList<>();

    public Game() {}

    public Game(JSONObject jGame) {
        this.stopped = jGame.getBoolean("stopped");
        this.currentPlayerIndex = jGame.getInt("currentPlayerIndex");
        boolean tradicional = jGame.getBoolean("gameMode");
        if (tradicional) {
            this.gameMode = Mode.Traditional;
        } else {
            this.gameMode = Mode.Fast;
        }
        JSONArray jPlayers = jGame.getJSONArray("players");
        for (int i = 0; i < jPlayers.length(); ++i) {
            JSONObject jPlayer = jPlayers.getJSONObject(i);
            Player auxPlayer = new Player(jPlayer, this.board);
            this.players.add(auxPlayer);
        }
    }

    /* Getters */

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

    public Player getWinner() {
        return winner;
    }

    public void setStopped(boolean stopped, Player winner) {
        this.stopped = stopped;
        if (winner != null) {
            setWinner(winner);
        }
        for (GameObserver i : observers) {
            i.onGameEnded(this);
        }
    }

    public void setStopped(boolean stopped) {
        setStopped(stopped, null);
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

    public void setWinner(Player player) {
        winner = player;
    }

    public void addObserver(GameObserver observer) {
        observer.onRegister(this);
        observers.add(observer);
    }

    /* Métodos */

    public void saveGame(File file) {
        JSONObject jGame = this.toJSON();

        try {
            FileWriter exit = new FileWriter(file);
            exit.append(jGame.toString());
            exit.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Game loadGame(File file) {
        JSONObject jGame = null;
        try {
            jGame = new JSONObject(new JSONTokener(new FileInputStream(file)));
        } catch (Exception ex) {} //Cambiar luego

        return new Game(jGame);
    }

    public void addNewPlayer(PieceColor color, Board.Side side)
        throws OccupiedCellException {
        this.players.add(
                new Player(color, side, new Integer(players.size() + 1).toString())
            );
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
     * @return Tiempo en milisegundos que el jugador actual lleva jugando
     */
    public long getCurrentPlayerTime() {
        return this.getCurrentPlayer().timePlaying();
    }

    /**
     * Lleva a cabo instruccionees basicas para empezar una partida
     */
    public void start() {
        this.stopped = false;
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
        //TODO evitar que se atasque
        this.endTurn();
        do {
            this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
        } while (this.getCurrentPlayer().hasSurrender());
        for (GameObserver i : observers) {
            i.onEndTurn(this);
        }
        this.startTurn();
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
            sendOnSelectedPiece(this.getCurrentPlayer().getSelectedPiece());
        }
        return out;
    }

    public boolean isSelectedPieceIn(Cell position) {
        return this.getCurrentPlayer().getSelectedPiece().getPosition() == position;
    }

    public void deselectPiece() {
        Piece selected = this.getCurrentPlayer().getSelectedPiece();
        this.getCurrentPlayer().deselectPiece();
        sendOnSelectedPiece(selected);
    }

    public boolean hasSelectedPiece() {
        return this.getCurrentPlayer().hasSelectedPiece();
    }

    public void sendOnSelectedPiece(Piece piece) {
        for (GameObserver i : observers) {
            i.onSelectedPiece(piece);
        }
    }

    public void sendOnMovedPiece(Cell from, Cell to, String playerID) {
        for (GameObserver i : observers) {
            i.onMovedPiece(from, to, playerID);
        }
    }

    public void reset() {
        board = new Board();
        stopped = false;
        players = new ArrayList<>();
        observers = new ArrayList<>();
        winner = null;
    }

    public JSONObject toJSON() {
        JSONObject jRes = new JSONObject();
        jRes.put("stopped", this.stopped);
        jRes.put("currentPlayerIndex", this.currentPlayerIndex);
        jRes.put("gameMode", this.gameMode == Mode.Traditional);

        JSONArray jPlayers = new JSONArray();
        for (int i = 0; i < players.size(); ++i) {
            jPlayers.put(this.players.get(i).toJSON());
        }

        jRes.put("players", jPlayers);

        return jRes;
    }

    public void softReset() {
        this.board  = new Board();
        this.stopped = false;
        this.currentPlayerIndex = 0;
        this.winner = null;
        
        for(Player player : players) {
            player.softReset();
        }
        
        this.observers = new ArrayList<GameObserver>();
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
            Cell from = selectedPiece.getPosition();
            selectedPiece.move(to, getGameMode());
            sendOnMovedPiece(from, to, getCurrentPlayer().getId());

            Player currentPlayer = getCurrentPlayer();
            if (currentPlayer.isAWinner()) {
                winner = currentPlayer;
            }
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

    // Para Debug
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
