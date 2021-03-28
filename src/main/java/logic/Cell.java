package logic;

import exceptions.CellsNotLinedUpException;
import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import exceptions.OutOfBoundsException;
import logic.gameObjects.Piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Cell {
    private final int row;
    private final int col;

    private Piece piece;
    private Board board;

    // Convertir en su propia clase
    public enum Direction {
        Right, LowerRight, LowerLeft, Left, UpperLeft, UpperRight
    }

    /**
     * Crea una celda en la posición (row, col)
     *
     * @param row
     * @param col
     * @param board
     * @throws OutOfBoundsException
     */
    public Cell(int row, int col, Board board) {
        this.row = row;
        this.col = col;
        this.board = board;
        this.piece = null;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public Piece getPiece() {
        return this.piece;
    }

    public void putPiece(Piece piece) throws OccupiedCellException {
        if (this.piece != null) {
            /// La celda está ocupada
            throw new OccupiedCellException();
        }

        this.piece = piece;
    }

    /**
     * Quitar la pieza de la celda
     * <p>
     * Este método tiene que ser invocado desde pieza, para actualizar al mismo
     * tiempo su referencia a la nueva celda. Sino, la pieza quedaría posicionada a
     * una celda inválida.
     *
     * @throws InvalidOperationException cuando no hay pieza en la celda
     */
    public void removePiece() throws InvalidOperationException {
        if (this.piece == null) {
            throw new InvalidOperationException("No hay pieza en esta posición");
        }

        /// Si había una pieza puesta en esta posición, hay que cortar las referencias
        this.piece = null;
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
        ArrayList<Cell> ret = new ArrayList<>();

        for (Direction d : Direction.values()) {
            Cell c = this.getByDirection(d, dist);
            if (c != null)
                ret.add(c);
        }
        // No puedes convertir de tipo List<Cell> a List
        return Collections.unmodifiableList(ret);
    }

    public List<Cell> getNeighbours() {
        return getNeighbours(1);
    }

    public Cell getUpperRight() {
        return getUpperRight(1);
    }

    public Cell getUpperRight(int times) {
        if (row % 2 == 1) // Fila impar
            return this.board.getCell(row - times, col + times / 2);
        else // Fila par
            return this.board.getCell(row - times, col + (times + 1) / 2);
    }

    public Cell getUpperLeft() {
        return getUpperLeft(1);
    }

    public Cell getUpperLeft(int times) {
        if (row % 2 == 1) // Fila impar
            return this.board.getCell(row - times, col - (times + 1) / 2);
        else // Fila par
            return this.board.getCell(row - times, col - times / 2);
    }

    public Cell getLowerRight() {
        return getLowerRight(1);
    }

    public Cell getLowerRight(int times) {
        if (row % 2 == 1) // Fila impar
            return this.board.getCell(row + times, col + times / 2);
        else // Fila par
            return this.board.getCell(row + times, col + (times + 1) / 2);
    }

    public Cell getLowerLeft() {
        return getLowerLeft(1);
    }

    public Cell getLowerLeft(int times) {
        if (row % 2 == 1) // Fila impar
            return this.board.getCell(row + times, col - (times + 1) / 2);
        else // Fila par
            return this.board.getCell(row + times, col - times / 2);
    }

    public Cell getRight() {
        return getRight(1);
    }

    public Cell getRight(int times) {
        return this.board.getCell(row, col + times);
    }

    public Cell getLeft() {
        return getLeft(1);
    }

    public Cell getLeft(int times) {
        return this.board.getCell(row, col - times);
    }

    // Package-private para que puedan usarse en tests
    Cell getByDirection(Direction dir) {
        return getByDirection(dir, 1);
    }

    Cell getByDirection(Direction dir, int dist) {
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
            } else { // this.getRow() > other.getRow() // other está por arriba
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

    public Cell getMiddleCellTowards(Cell other) // TODO: Puede que no sea necesario, deprecable
            throws CellsNotLinedUpException, InvalidOperationException {
        Direction dir = this.getDirectionTowards(other);
        int times = this.getDiagonalDistanceTo(other);

        if (times % 2 == 1)
            throw new InvalidOperationException("There is no middle cell.");
        else {
            return this.getByDirection(dir, times / 2); // Shouldn't throw OutOfBoundsException
        }
    }

    /**
     * Devuelve un camino iterable en una dirección
     *
     * @param direccion Dirección que sigue el camino
     * @return
     */
    public Iterable<Cell> getTrail(Direction direccion) {
        return new Iterable<Cell>() {
            public Iterator<Cell> iterator() {
                return new Iterator<Cell>() {
                    private Cell pos = Cell.this;
                    private Direction dir = direccion;

                    @Override
                    public boolean hasNext() {
                        return pos.getByDirection(dir) != null;
                    }

                    @Override
                    public Cell next() {
                        Cell prev = pos;
                        if (hasNext())
                            pos = pos.getByDirection(dir);
                        return prev;
                    }

                };
            }
        };

    }

    /**
     * Devuelve un camino iterable entre dos celdas
     *
     * @param other Otra celda con la que comparar
     * @return
     * @throws CellsNotLinedUpException
     */
    public Iterable<Cell> getTrail(Cell other) throws CellsNotLinedUpException {
        if (!other.isInSameDiagonalAs(other))
            throw new CellsNotLinedUpException();

        int _left = this.getDiagonalDistanceTo(other);
        Direction _dir = this.getDirectionTowards(other);
        return new Iterable<Cell>() {
            @Override
            public Iterator<Cell> iterator() {
                return new Iterator<Cell>() {
                    private Cell pos = Cell.this;
                    private int left = _left;
                    private Direction dir = _dir;

                    @Override
                    public boolean hasNext() {
                        return left == 0;
                    }

                    @Override
                    public Cell next() {
                        left--;
                        Cell prev = pos;
                        if (hasNext())
                            pos = pos.getByDirection(dir);
                        return prev;
                    }

                };
            }
        };
    }

    @Deprecated
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
     * diagonal
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

    @Override
    public boolean equals(Object other) {
        try {
            Cell that = (Cell) other;
            return this.getCol() == that.getCol() && this.getRow() == that.getRow();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Comprobar que la celda está vacía para poner una pieza
     *
     * @return si no hay pieza, y es una posición dentro del tablero
     */
    public boolean isEmpty() {
        return this.piece == null;
    }

    public Cell getCellJump(Cell middleCell) {
        List<Cell> neighbours = middleCell.getNeighbours();

        for (Cell ady : neighbours) {
            if (ady.isInSameDiagonalAs(this)) {
                return ady;
            }
        }

        return null;
    }

    public boolean isOut() {
        return !board.insideBoard(row, col);
    }

}