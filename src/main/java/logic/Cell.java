package logic;

import exceptions.OutOfBoundsException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Cell {
    private int row, col;
    private static Board board; // TODO: Lo dejamos como estático? Y si queremos tener varias instancias del juego, o varios tableros?

    public Cell(int row, int col, Board board) throws OutOfBoundsException {
        if (!board.validRowColumn(row, col))
            throw new OutOfBoundsException(row, col);
        this.row = row;
        this.col = col;
        Cell.board = board;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void assign(Board.Color color) {
        try {
            this.board.put(row, col, color);
        } catch (OutOfBoundsException e) {
        }
    }

    public void remove() {
        this.board.remove(row, col);
    }

    public List<Cell> getNeighbours() {
        // Devuelve las celdas, donde 0 es R, y siguen en el sentido de 
        // las agujas del reloj
        ArrayList<Cell> ret = new ArrayList<Cell>();
        try {
            ret.add(this.getRight());
        } catch (Exception e) {
        }
        try {
            ret.add(this.getLowerRight());
        } catch (Exception e) {
        }
        try {
            ret.add(this.getLowerLeft());
        } catch (Exception e) {
        }
        try {
            ret.add(this.getLeft());
        } catch (Exception e) {
        }
        try {
            ret.add(this.getUpperLeft());
        } catch (Exception e) {
        }
        try {
            ret.add(this.getUpperRight());
        } catch (Exception e) {
        }
        // No puedes convertir de tipo List<Cell> a List
        return Collections.unmodifiableList(ret);
    }

    public Cell getUpperRight() throws OutOfBoundsException {
        if (row % 2 == 1) // Fila impar
            return new Cell(row - 1, col, board);
        else  // Fila par
            return new Cell(row - 1, col + 1, board);
    }

    public Cell getUpperLeft() throws OutOfBoundsException {
        if (row % 2 == 1) // Fila impar
            return new Cell(row - 1, col - 1, board);
        else  // Fila par
            return new Cell(row - 1, col, board);
    }

    public Cell getLowerRight() throws OutOfBoundsException {
        if (row % 2 == 1) // Fila impar
            return new Cell(row + 1, col, board);
        else  // Fila par
            return new Cell(row + 1, col + 1, board);
    }

    public Cell getLowerLeft() throws OutOfBoundsException {
        if (row % 2 == 1) // Fila impar
            return new Cell(row + 1, col - 1, board);
        else  // Fila par
            return new Cell(row + 1, col, board);
    }

    public Cell getRight() throws OutOfBoundsException {
        return new Cell(row, col + 1, board);
    }

    public Cell getLeft() throws OutOfBoundsException {
        return new Cell(row, col - 1, board);
    }

}