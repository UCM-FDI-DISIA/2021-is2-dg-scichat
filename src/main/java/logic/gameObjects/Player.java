package logic.gameObjects;

import exceptions.InvalidMoveException;
import exceptions.NotSelectedPieceException;
import exceptions.OccupiedCellException;
import logic.Board.Side;
import logic.Cell;

import java.awt.*;
import java.util.HashSet;

public class Player {
    private Color color; // Color asignado al jugador
    private HashSet<Piece> pieces = new HashSet<>(); // Fichas del jugador
    private Side playerSide;    // Lado del jugador
    private Piece selectedPiece = null;    // Pieza seleccionada
    private boolean surrender;    //Jugador se ha rendido

    /*Constructores*/

    //Constructor para debug exclusivamente
    public Player() throws OccupiedCellException {
        this.color = Color.BLUE;
        this.playerSide = Side.Down;
        this.surrender = false;
        createPieces();
    }

    public Player(Color color, Side start) throws OccupiedCellException {
        this.color = color;
        this.playerSide = start;
        this.surrender = false;
        createPieces();
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
    public Color getColor() {
        return color;
    }

    public Side getSide() {
        return playerSide;
    }

    /*Metodos*/

    /**
     * Pone piece como pieza seleccionada por el jugador con la que realizara varias
     * de sus acciones
     *
     * @param piece Piece que el jugador quiere seleccionar
     * @return true si es suya y puede seleccionarla, false si no
     */
    boolean selectPiece(Piece piece) {
        if (!pieces.contains(piece))
            return false;
        this.selectedPiece = piece;
        return true;
    }

    /**
     * Deselecciona la ficha seleccionada si tiene alguna fica seleccionada, si no
     * no hace nada
     */
    void deselectPiece() {
        this.selectedPiece = null;
    }

    /**
     * Devuelve si el jugador tiene una pieza seleccionada
     *
     * @return true si el jugador tiene una pieza seleccionada, false si no
     */
    boolean hasSelectedPiece() {
        return this.selectedPiece != null;
    }

    /**
     * @return true si todas las fichas del jugador se encuentran en posiciones
     * ganadoras
     */
    public boolean isAWinner() {
        for (Piece pc : pieces)
            if (!playerSide.getOpposingCells().contains(pc.getPosition())) {
                return false;
            }
        return true;
    }

    /**
     * Intenta mover la pieza seleccionada a la celda targetPosition
     *
     * @param targetPosition Cell a donde mover la pieza seleccionada
     * @throws NotSelectedPieceException
     * @throws InvalidMoveException
     */
    void move(Cell targetPosition) throws NotSelectedPieceException, InvalidMoveException {
        if (!this.hasSelectedPiece()) throw new NotSelectedPieceException();
        this.selectedPiece.move(targetPosition);
    }

    /**
     * El jugador empieza seleccionando una ficha pasando su casilla como argumento,
     * una vez que tiene una ficha seleccionada selecciona una casilla vacia para moverse
     * si el movimiento es valido devuelve true, en cualquier otro caso devuelve false.
     * <p>
     * Si selecciona una celda ocupada cuando ya tiene pieza seleccionada y
     * la celda contiene una pieza suya, cambia la pieza seleccionada,
     * si la pieza de la celda no es suya, deselecciona la pieza
     *
     * @param targetPosition Posicion que el jugador selecciona
     * @return True si el jugador ha hecho una jugada (movido una ficha), false si no
     */
    public boolean turn(Cell targetPosition) {
        if (targetPosition.isEmpty()) {
            try {
                this.move(targetPosition);
            } catch (Exception e) {
                return false;
            }
            deselectPiece();
            return true;
        } else {
            if (!selectPiece(targetPosition.getPiece())) {
                deselectPiece();
            }
            return false;
        }
    }

    public boolean hasSurrender() {
        return surrender;
    }

    public void surrender() {
        this.surrender = true;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();

        result.append(String.format("Color: %s", this.color));
        result.append(String.format("Posición: %s", this.playerSide));

        return result.toString();
    }

    public String piecesToString() {
        StringBuilder result = new StringBuilder();
        result.append("Piezas disponibles:");

        for (Piece piece : this.pieces) {
            result.append(String.format("   (%s, %s) \n", piece.getPosition().getRow(), piece.getPosition().getCol()));
        }

        return result.toString();
    }
}