package logic.gameObjects;

import exceptions.InvalidMoveException;
import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import exceptions.OutOfBoundsException;
import logic.Board;
import logic.Cell;
import logic.Color;

import java.util.List;

public class Piece {

    // El color es lo que hace que una ficha pertenezca a un jugador
    private final Color color;
    private Cell position;

    public Color getColor() {
	return color;
    }

    public Piece(Cell pos, Color color) throws OccupiedCellException {
	this.color = color;

	if (!pos.isEmpty())
	    throw new OccupiedCellException(pos);
	this.position = pos;
	this.position.putPiece(this);
    }

    public Cell getPosition() {
	return this.position;
    }

    /**
     * Comprueba si se puede llevar a cabo un determinado movimiento
     * 
     * @param targetPosition Celda a la que queremos mover la ficha
     * @throws InvalidOperationException Salta cuando el Movimiento que queremos
     *                                   llevar a cabo no es posible
     * @throws OutOfBoundsException      Salta cuando la posicion a moverse no es
     *                                   posible
     */
    public void tryToMoveTo(Cell targetPosition) throws InvalidMoveException, OutOfBoundsException {
	boolean[][] checkedCells = new boolean[Board.NUM_ROW][Board.NUM_COL]; // No se a que se inicializan, asumo que
									      // false, sino luego lo cambio
	if (targetPosition.isOut()) {
	    throw new OutOfBoundsException();
	}
	if (targetPosition.equals(this.position)) {
	    throw new InvalidMoveException();
	}

	List<Cell> neighbours = this.position.getNeighbours();

	if (!neighbours.contains(targetPosition)) {

	    for (int i = 0; i < Board.NUM_ROW; ++i) {
		for(int j = 0; j < Board.NUM_COL; ++j)
		    checkedCells[i][j] = false;
	    }

	    if (!recursiveTryToMoveTo(targetPosition, this.position, checkedCells)) {
		throw new InvalidMoveException();
	    }
	}

    }

    /**
     * Método recursivo que nos devuelve true si se puede alcancar la posición o no
     * 
     * @param targetPosition  Posición que queremos alcanzar
     * @param positionToCheck Posición en la que nos encontramos actualmente
     * @param checkedCells    Tablero con las Celdas que ya hemos mirado
     * @return Devuelve si se puede llevar a cabo el movimiento
     */
    private boolean recursiveTryToMoveTo(Cell targetPosition, Cell positionToCheck, boolean[][] checkedCells) {
	List<Cell> neighbours = positionToCheck.getNeighbours();

	for (Cell ady : neighbours) {

	    if (!checkedCells[ady.getRow()][ady.getCol()]) {

		checkedCells[ady.getRow()][ady.getCol()] = true;

		Cell newJump = positionToCheck.getCellJump(ady);
		
		if (newJump != null && (newJump.equals(targetPosition) || (recursiveTryToMoveTo(targetPosition, newJump, checkedCells)))) {
		    return true;
		}

		checkedCells[ady.getRow()][ady.getCol()] = false;

	    }

	}

	return false;

    }

    /**
     * Desplazar la pieza
     *
     * @param targetPosition posición destino
     * @throws InvalidOperationException puede ser que sea una posición ocupada o un
     *                                   movimiento inválido
     * @throws OutOfBoundsException
     */
    public void move(Cell targetPosition) throws InvalidMoveException {
	try {
	    tryToMoveTo(targetPosition);
	    this.position.removePiece();
	    this.position = targetPosition;
	    this.position.putPiece(this);
	} catch (Exception e) {
	    System.out.println(e.getMessage());
	    throw new InvalidMoveException("The move is not possible.");
	}
    }

}