package logic.gameObjects;

import logic.Board.Side;
import logic.Cell;
import logic.Color;

import java.util.HashSet;

import exceptions.OccupiedCellException;

public class Player {
    private Color color; // Color asignado al jugador
    private HashSet<Piece> pieces = new HashSet<>(); // Fichas del jugador
    private Side playerSide;    // Lado del jugador
    private Piece selectedPiece = null;    // Pieza seleccionada

    /*Constructores*/

    //Constructor para debug exclusivamente
    public Player() throws OccupiedCellException {
        color = Color.Blue;
        playerSide=Side.Down;
        createPieces();
    }

    public Player(Color color, Side start) throws OccupiedCellException {
        this.color = color;
        playerSide=start;
        createPieces();
    }
    
    /**
     * Genera las fichas de Player, solo se llama una vez al principio de la partida
     * 
     * @throws OccupiedCellException si la zona en la que empieza esta ocupada
     */
    private void createPieces() throws OccupiedCellException {
    	HashSet<Cell> in=this.playerSide.getSideCells();
    	for(Cell i: in) {
    		Piece aux=new Piece(i,this.color);
    		pieces.add(aux);
    		i.putPiece(aux);
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
    public boolean selectPiece(Piece piece) {
        if (!pieces.contains(piece))
            return false;
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
        for (Piece pc : pieces)
            if (!playerSide.getOpposingCells().contains(pc.getPosition())) {
                return false;
            }
        return true;
    }
}