package logic.gameObjects;

import exceptions.InvalidMoveException;
import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import exceptions.OutOfBoundsException;
import logic.Board;
import logic.Cell;
import logic.Color;

import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

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
	boolean[][] checkedCells = new boolean[Board.NUM_ROW][Board.NUM_COL];

	if (targetPosition.isOut()) {
	    throw new OutOfBoundsException();
	}
	if (targetPosition.equals(this.position)) {
	    throw new InvalidMoveException();
	}

	List<Cell> neighbours = this.position.getNeighbours();

	if (!neighbours.contains(targetPosition)) {

	    for (int i = 0; i < Board.NUM_ROW; ++i) {
		for (int j = 0; j < Board.NUM_COL; ++j)
		    checkedCells[i][j] = false;
	    }

	    Queue<Cell> positionQueue = new LinkedList<Cell>();
	    positionQueue.add(this.position);

	    if (!recursiveTryToMoveTo(targetPosition, checkedCells, positionQueue)) {
		throw new InvalidMoveException("No se puede acceder a la casilla " + targetPosition);
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
    private boolean recursiveTryToMoveTo(Cell targetPosition, boolean[][] checkedCells, Queue<Cell> positionQueue) {

	Cell positionToCheck = positionQueue.peek();
	if (!checkedCells[positionToCheck.getRow()][positionToCheck.getCol()]) {
	    positionQueue.poll();
	    List<Cell> neighbours = positionToCheck.getNeighbours();
	    
	    for(Cell ng : neighbours) {
		if(!ng.isEmpty()) {	System.out.println("Vecino " + ng);	}
	    }

	    for (Cell ady : neighbours) {
		Cell newJump = positionToCheck.getCellJump(ady);
		if (newJump != null) {
		    System.out.println("Nuevo salto " + newJump + " desde la posicion " + positionToCheck + ", saltando " + ady);
		    if (newJump.equals(targetPosition))
			return true;
		    positionQueue.add(newJump);
		}
	    }

	    checkedCells[positionToCheck.getRow()][positionToCheck.getCol()] = true;

	    return recursiveTryToMoveTo(targetPosition, checkedCells, positionQueue);
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