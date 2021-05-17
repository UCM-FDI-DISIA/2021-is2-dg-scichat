package logic;

import control.options.Option.ExecuteException;
import exceptions.InvalidMoveException;
import exceptions.OccupiedCellException;
import graphic.GameObserver;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import logic.Board.Side;
import logic.gameObjects.HumanPlayer;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import utils.Mode;
import utils.PieceColor;

public class Game {
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
    private long timePlaying = 0;
    private long timeAtTurnStart = 0;
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
        this.timePlaying = jGame.getLong("time");

        JSONArray jPlayers = jGame.getJSONArray("players");
        for (int i = 0; i < jPlayers.length(); ++i) {
            JSONObject jPlayer = jPlayers.getJSONObject(i);

            //Inicializamos a cada uno de los jugadores
            HashSet<Piece> auxPieces = new HashSet<Piece>();
            JSONArray jPieces = jPlayer.getJSONArray("pieces");

            for (int j = 0; j < jPieces.length(); ++j) {
                JSONObject jPiece = jPieces.getJSONObject(j);
                Cell auxCell = board.getCell(jPiece.getInt("row"), jPiece.getInt("col"));
                Piece auxPiece = null;
                try {
                    auxPiece =
                        new Piece(
                            auxCell,
                            PieceColor.getPieceColor(jPlayer.getInt("color"))
                        );
                } catch (OccupiedCellException ex) {}
                auxPieces.add(auxPiece);
            }

            Player auxPlayer = new HumanPlayer(
                PieceColor.getPieceColor(jPlayer.getInt("color")),
                Side.getSide(jPlayer.getInt("playerSide")),
                jPlayer.getString("id"),
                jPlayer.getBoolean("playing"),
                auxPieces,
                jPlayer.getBoolean("surrender")
            );

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
                new HumanPlayer(color, side, new Integer(players.size() + 1).toString())
            );
    }

    /*Metodos de control de tiempo de juego*/

    public long getTimePlaying() {
        return this.timePlaying - this.timeAtTurnStart + System.currentTimeMillis();
    }

    /**
     * @return Tiempo en milisegundos que el jugador actual lleva jugando
     */
    public long getCurrentTime() {
        return this.getTimePlaying();
    }

    /**
     * Lleva a cabo instruccionees basicas para empezar una partida
     */
    public void start() {
        this.stopped = false;
        this.timeAtTurnStart = System.currentTimeMillis();
        for (GameObserver i : this.observers) i.onGameStart(this);
    }

    public Player currentPlayerSurrender() {
        this.getCurrentPlayer().surrender();
        Player winner = wonBySurrender();
        if (winner != null) setStopped(true, winner);
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
            if (!i.hasSurrendered()) {
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
        do {
            this.currentPlayerIndex = (this.currentPlayerIndex + 1) % this.players.size();
        } while (this.getCurrentPlayer().hasSurrendered());
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

    public Set<Piece> getCurrentPlayerPieces() {
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
        this.timePlaying = 0;
    }

    public JSONObject toJSON() {
        JSONObject jRes = new JSONObject();
        jRes.put("stopped", this.stopped);
        jRes.put("currentPlayerIndex", this.currentPlayerIndex);
        jRes.put("gameMode", this.gameMode == Mode.Traditional);
        jRes.put("time", this.getCurrentTime());

        JSONArray jPlayers = new JSONArray();
        for (int i = 0; i < players.size(); ++i) {
            jPlayers.put(this.players.get(i).toJSON());
        }

        jRes.put("players", jPlayers);

        return jRes;
    }

    /**
     * Nos permite resetear el juego para dejarlo en el estado inicial de la partida.
     */
    public void softReset() {
        this.board = new Board();
        this.stopped = false;
        this.currentPlayerIndex = 0;
        this.winner = null;
        this.timePlaying = 0;

        for (Player player : players) {
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
            currentPlayer.deselectPiece();
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
            DurationFormatUtils.formatDuration(this.getTimePlaying(), "HH:mm:ss.S") +
            "\n"
        );
        return result.toString();
    }
}
