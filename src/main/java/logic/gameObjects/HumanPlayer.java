package logic.gameObjects;

import exceptions.InvalidMoveException;
import exceptions.NotSelectedPieceException;
import exceptions.OccupiedCellException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import logic.Board;
import logic.Board.Side;
import logic.Cell;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Mode;
import utils.PieceColor;

public class HumanPlayer implements Serializable, Player {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private PieceColor color; // Color asignado al jugador
    private HashSet<Piece> pieces = new HashSet<>(); // Fichas del jugador
    private Side playerSide; // Lado del jugador
    private Piece selectedPiece = null; // Pieza seleccionada
    private boolean surrender; //Jugador se ha rendido
    private String id; // Jugador numero id
    private boolean playing = false;
    private String name;

    /*Constructores*/

    //Constructor para debug exclusivamente
    public HumanPlayer() throws OccupiedCellException {
        this(PieceColor.BLUE, Side.Down, "0");
    }

    public HumanPlayer(PieceColor color, Side start, String id)
        throws OccupiedCellException {
        this.color = color;
        this.playerSide = start;
        this.surrender = false;
        this.id = id;
        createPieces();
    }

    public HumanPlayer(
        PieceColor color,
        Side playerSide,
        String id,
        boolean playing,
        HashSet<Piece> pieces,
        boolean surrender
    ) {
        this.color = color;
        this.pieces = pieces;
        this.playerSide = playerSide;
        this.surrender = surrender;
        this.id = id;
        this.playing = playing;
    }

    public HumanPlayer(JSONObject jPlayer, Board board) {
        this.color = PieceColor.getPieceColor(jPlayer.getInt("color")); //Crear metodo de color
        this.playerSide = Side.getSide(jPlayer.getInt("playerSide")); //Crear metodo de Side
        this.surrender = jPlayer.getBoolean("surrender");
        this.id = jPlayer.getString("id");
        this.playing = jPlayer.getBoolean("playing");

        //Ahora creamos las piezas correspondientes
        JSONArray jPieces = jPlayer.getJSONArray("pieces");

        for (int i = 0; i < jPieces.length(); ++i) {
            JSONObject jPiece = jPieces.getJSONObject(i);
            Cell auxCell = board.getCell(jPiece.getInt("row"), jPiece.getInt("col"));
            Piece auxPiece = null;
            try {
                auxPiece = new Piece(auxCell, this.color);
            } catch (OccupiedCellException ex) {}
            this.pieces.add(auxPiece);
        }
    }

    /**
     * Genera las fichas de Player, solo se llama una vez al principio de la partida
     *
     * @throws OccupiedCellException si la zona en la que empieza esta ocupada
     */
    private void createPieces() throws OccupiedCellException {
        HashSet<Cell> in = this.playerSide.getSideCells();
        for (Cell i : in) {
            Piece aux = new Piece(i, this.color);
            pieces.add(aux);
        }
    }

    /* Getters */
    public PieceColor getColor() {
        return color;
    }

    public Side getSide() {
        return playerSide;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        if (name == null) return id;
        return name;
    }

    public HashSet<Piece> getPieces() {
        return pieces;
    }

    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    /**
     * Pone piece como pieza seleccionada por el jugador con la que realizara varias
     * de sus acciones
     *
     * @param piece Piece que el jugador quiere seleccionar
     * @return true si es suya y puede seleccionarla, false si no
     */
    public boolean selectPiece(Piece piece) {
        if (piece == null || !pieces.contains(piece)) return false;
        this.selectedPiece = piece;
        return true;
    }

    /**
     * Nos permite resetear el player para dejarlo en el estado inicial que nos lo encontramos
     * al iniciar la partida.
     */
    public void softReset() {
        this.playing = false;
        this.selectedPiece = null;
        this.surrender = false;

        this.pieces = new HashSet<Piece>();
        try {
            this.createPieces();
        } catch (OccupiedCellException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Deselecciona la ficha seleccionada si tiene alguna fica seleccionada, si no
     * no hace nada
     */
    public void deselectPiece() {
        this.selectedPiece = null;
    }

    /**
     * Devuelve si el jugador tiene una pieza seleccionada
     *
     * @return true si el jugador tiene una pieza seleccionada, false si no
     */
    public boolean hasSelectedPiece() {
        return this.selectedPiece != null;
    }

    /**
     * @return true si todas las fichas del jugador se encuentran en posiciones
     * ganadoras
     */
    public boolean isAWinner() {
        for (Piece pc : pieces) if (
            !playerSide.getOpposingCells().contains(pc.getPosition())
        ) {
            return false;
        }
        return true;
    }

    /**
     * Intenta mover la pieza seleccionada a la celda targetPosition
     *
     * @param targetPosition Cell a donde mover la pieza seleccionada
     * @param
     * @throws NotSelectedPieceException
     * @throws InvalidMoveException
     */
    public void move(Cell targetPosition, Mode playMode)
        throws NotSelectedPieceException, InvalidMoveException {
        if (!this.hasSelectedPiece()) throw new NotSelectedPieceException();
        this.selectedPiece.move(targetPosition, playMode);
    }

    public boolean hasSurrendered() {
        return surrender;
    }

    public void surrender() {
        this.surrender = true;
    }

    /**
     * Imprime el color y posición en el tablero del jugador
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Color: %s", this.color));
        result.append(String.format("Posición: %s", this.playerSide));

        return result.toString();
    }

    /**
     * Hacer una lista de piezas disponibles del jugador, con sus coordenadas
     *
     * @return string con piezas disponibles del jugador
     */

    public boolean hasPiece(Piece piece) {
        return this.pieces.contains(piece);
    }

    public JSONObject toJSON() {
        JSONObject jPlayer = new JSONObject();
        jPlayer.put("color", this.color.getJSONValue());
        jPlayer.put("playerSide", this.playerSide.getJSONValue());
        jPlayer.put("surrender", this.surrender);
        jPlayer.put("id", this.id);
        jPlayer.put("playing", this.playing);

        JSONArray jPieces = new JSONArray();

        for (Piece piece : pieces) {
            jPieces.put(piece.toJSON());
        }
        jPlayer.put("pieces", jPieces);

        return jPlayer;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
