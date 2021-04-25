package logic.gameObjects;

import exceptions.InvalidMoveException;
import exceptions.NotSelectedPieceException;
import exceptions.OccupiedCellException;
import java.awt.*;
import java.io.Serializable;
import java.util.HashSet;
import logic.Board.Side;
import logic.Cell;
import utils.Mode;
import utils.PieceColor;

public class Player implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private PieceColor color; // Color asignado al jugador
    private HashSet<Piece> pieces = new HashSet<>(); // Fichas del jugador
    private Side playerSide; // Lado del jugador
    private Piece selectedPiece = null; // Pieza seleccionada
    private boolean surrender; //Jugador se ha rendido
    private int id; //Jugador numero id
    private long time; //Tiempo que lleva jugando
    private boolean playing=false;

    private long timeAtTurnStart;

    /*Constructores*/

    //Constructor para debug exclusivamente
    public Player() throws OccupiedCellException {
        this(PieceColor.BLUE, Side.Down, 0);
    }

    public Player(PieceColor color, Side start, int id) throws OccupiedCellException {
        this.color = color;
        this.playerSide = start;
        this.surrender = false;
        this.id = id;
        this.time = 0;
        createPieces();
    }

    /**
     * Genera las fichas de Player, solo se llama una vez al principio de la partida
     *
     * @param playMode Reglas de movimiento de las fichas
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

    public int getId() {
        return id;
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

    public boolean hasSurrender() {
        return surrender;
    }

    public void surrender() {
        this.surrender = true;
    }

    /**
     * Funcion para medir el tiempo de juego
     */
    public void startTurn() {
        this.timeAtTurnStart = System.currentTimeMillis();
        playing=true;
    }

    /**
     * Funcion para medir el tiempo de juego
     */
    public void endTurn() {
        this.time += System.currentTimeMillis() - this.timeAtTurnStart;
        playing=false;
    }

    public long timePlaying() {
        if(playing)return time + System.currentTimeMillis() - this.timeAtTurnStart;
        else return time;
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

    public Boolean hasPiece(Piece piece) {
        return this.pieces.contains(piece);
    }
}
