package logic;

import exceptions.CellsNotLinedUpException;
import exceptions.InvalidOperationException;
import exceptions.OutOfBoundsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Cell {
	private int row, col;
	public static Board board; // TODO: Lo dejamos como estático? Y si queremos tener varias instancias del
								// juego, o varios tableros?

	public enum Direction {
		Right, LowerRight, LowerLeft, Left, UpperLeft, UpperRight
	}

	/**
	 * Crea una celda en la posición (row, col)
	 * @param row
	 * @param col
	 * @param board
	 * @throws OutOfBoundsException
	 */
	public Cell(int row, int col, Board board) throws OutOfBoundsException {
		if (!board.validRowColumn(row, col))
			throw new OutOfBoundsException(row, col);
		this.row = row;
		this.col = col;
		Cell.board = board;
	}

	public Cell(int row, int col) {
		this.col = col;
		this.row = row;
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public List<Cell> getNeighbours() {
		return getNeighbours(1);
	}

	/**
	 * Devuelve una lista inmutable de celdas, donde la primera es la de la derecha,
	 * y el resto siguen en el orden de las agujas del reloj
	 * 
	 * @param dist distancia de las celdas a coger
	 * @return lista inmutable con los vecinos
	 */
	public List<Cell> getNeighbours(int dist) {
		// Devuelve las celdas, donde 0 es R, y siguen en el sentido de
		// las agujas del reloj
		ArrayList<Cell> ret = new ArrayList<Cell>();
		try {
			ret.add(this.getRight(dist));
		} catch (Exception e) {
		}
		try {
			ret.add(this.getLowerRight(dist));
		} catch (Exception e) {
		}
		try {
			ret.add(this.getLowerLeft(dist));
		} catch (Exception e) {
		}
		try {
			ret.add(this.getLeft(dist));
		} catch (Exception e) {
		}
		try {
			ret.add(this.getUpperLeft(dist));
		} catch (Exception e) {
		}
		try {
			ret.add(this.getUpperRight(dist));
		} catch (Exception e) {
		}
		// No puedes convertir de tipo List<Cell> a List
		return Collections.unmodifiableList(ret);
	}

	public Cell getUpperRight() throws OutOfBoundsException {
		if (row % 2 == 1) // Fila impar
			return new Cell(row - 1, col, board);
		else // Fila par
			return new Cell(row - 1, col + 1, board);
	}

	public Cell getUpperLeft() throws OutOfBoundsException {
		if (row % 2 == 1) // Fila impar
			return new Cell(row - 1, col - 1, board);
		else // Fila par
			return new Cell(row - 1, col, board);
	}

	public Cell getLowerRight() throws OutOfBoundsException {
		if (row % 2 == 1) // Fila impar
			return new Cell(row + 1, col, board);
		else // Fila par
			return new Cell(row + 1, col + 1, board);
	}

	public Cell getLowerLeft() throws OutOfBoundsException {
		if (row % 2 == 1) // Fila impar
			return new Cell(row + 1, col - 1, board);
		else // Fila par
			return new Cell(row + 1, col, board);
	}

	public Cell getRight() throws OutOfBoundsException {
		return new Cell(row, col + 1, board);
	}

	public Cell getLeft() throws OutOfBoundsException {
		return new Cell(row, col - 1, board);
	}

	// -----------------------WIP------------------------- //
	public Cell getUpperRight(int times) throws OutOfBoundsException {
		if (row % 2 == 1) // Fila impar
			return new Cell(row - times, col + times/2, board);
		else // Fila par
			return new Cell(row - times, col + (times+1)/2, board);
	}

	public Cell getUpperLeft(int times) throws OutOfBoundsException {
		if (row % 2 == 1) // Fila impar
			return new Cell(row - times, col - (times+1)/2, board);
		else // Fila par
			return new Cell(row - times, col - times/2, board);
	}

	public Cell getLowerRight(int times) throws OutOfBoundsException {
		if (row % 2 == 1) // Fila impar
			return new Cell(row + times, col + times/2, board);
		else // Fila par
			return new Cell(row + times, col + (times+1)/2, board);
	}

	public Cell getLowerLeft(int times) throws OutOfBoundsException {
		if (row % 2 == 1) // Fila impar
			return new Cell(row + times, col - (times+1)/2, board);
		else // Fila par
			return new Cell(row + times, col - times/2, board);
	}

	public Cell getRight(int times) throws OutOfBoundsException {
		return new Cell(row, col + times, board);
	}

	public Cell getLeft(int times) throws OutOfBoundsException {
		return new Cell(row, col - times, board);
	}

	// Package-private para que puedan usarse en tests
	Cell getByDirection(Direction dir) throws CellsNotLinedUpException, OutOfBoundsException {
		return getByDirection(dir, 1);
	}

	Cell getByDirection(Direction dir, int dist) throws CellsNotLinedUpException, OutOfBoundsException {
		switch (dir) {
		case Right:
			return getRight(dist);
		case LowerRight:
			return getLowerRight(dist);
		case LowerLeft:
			return getLowerLeft(dist);
		case Left:
			return getLeft(dist);
		case UpperLeft:
			return getUpperLeft(dist);
		case UpperRight:
			return getUpperRight(dist);
		}
		return null; // Should never happen
	}

	// package private para test
	Direction getDirectionTowards(Cell other) throws CellsNotLinedUpException {
		if (this.isInSameDiagonalAs(other)) {
			if (this.getRow() == other.getRow()) { // Misma horizontal
				return (this.getCol() < other.getCol() ? Direction.Right : Direction.Left);
			} else if (this.getRow() < other.getRow()) { // other está por debajo
				return (this.getCol() < other.getCol() ? Direction.LowerRight : Direction.LowerLeft);
			} else { // this.getRow() > other.getRow()   // other está por arriba
				return (this.getCol() < other.getCol() ? Direction.UpperRight : Direction.UpperLeft);
			}
		} else
			throw new CellsNotLinedUpException("Cells do not line up.");
	}

	/**
	 * Devuelve la distancia de una celda en la diagonal de la actual a la actual.
	 * 
	 * @param other otra celda de la diagonal
	 * @return distancia diagonal entre celdas
	 * @throws InvalidOperationException
	 */
	public int getDiagonalDistanceTo(Cell other) throws CellsNotLinedUpException {
		if (this.isInSameDiagonalAs(other)) {
			return Math.max(Math.abs(this.getCol() - other.getCol()), Math.abs(this.getRow() - other.getRow()));
		} else
			throw new CellsNotLinedUpException("Both cells must be in the same diagonal.");
	}

	public Cell getMiddleCellTowards(Cell other)
			throws CellsNotLinedUpException, InvalidOperationException, OutOfBoundsException {
		Direction dir = this.getDirectionTowards(other);
		int times = this.getDiagonalDistanceTo(other);

		if (times % 2 == 1)
			throw new InvalidOperationException("There is no middle cell.");
		else {
			return this.getByDirection(dir, times / 2); // Shouldn't throw OutOfBoundsException
		}
	}

	public Iterator<Cell> getIteratorTowards(Cell other) throws CellsNotLinedUpException {
		return new Iterator<Cell>() {
			private Cell pos = Cell.this;
			private int left = Cell.this.getDiagonalDistanceTo(other);

			@Override
			public boolean hasNext() {
				return left == 0;
			}

			@Override
			public Cell next() {
				left--;
				Cell prev = pos;
				try {
					pos = pos.getByDirection(pos.getDirectionTowards(other));
				} catch (CellsNotLinedUpException e) {
					// No va a pasar nada, ya sabemos que están alineados
				} catch (OutOfBoundsException e) {
					return null;
				}
				return prev;
			}
		};
	}

	public String toString() {
		return String.format("(%d,%d)", row, col);
	}

	/* CHECKS Y FUNCIONES COMPROBADORAS */

	/**
	 * Comprueba si una celda está en la diagonal de la actual
	 * 
	 * @param other celda que comprobar
	 * @return verdadero si están en la misma diagonal falso si no están en la misma
	 *         diagonal
	 */
	public boolean isInSameDiagonalAs(Cell other) {

		// Básicamente, queremos comprobar si la celda `other`, con
		// coordenadas en el tablero (a,b), está en diagonal con
		// esta celda, de coordenadas (x,y)

		// Primero comprobamos que esten en la misma horizontal
		if (other.getRow() == this.getRow())
			return true;

		int distance = Math.abs(this.getRow() - other.getRow());
		for (Cell candidate : this.getNeighbours(distance)) {
			if (candidate.equals(other))
				return true;
		}
		return false;
	}

	public boolean isEmpty() {
		return this.board.available(row, col);
	}

	@Override
	public boolean equals(Object other) {
		try {
			Cell that = (Cell) other;
			return this.getCol() == that.getCol() && this.getRow() == that.getRow();
		}
		catch (Exception e) {
			return false;
		}
	}

	// -----------------------WIP------------------------- //

	/* MUTADORAS */

	public void assign(Board.Color color) {
		try {
			this.board.put(row, col, color);
		} catch (OutOfBoundsException e) {
		}
	}

	public void remove() {
		this.board.remove(row, col);
	}

}