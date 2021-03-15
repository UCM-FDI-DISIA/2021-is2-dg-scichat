package logic;

import exceptions.OutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CellTest {

    private Cell cell;
    private Board board;

    @BeforeEach
    void init() {
        board = new Board();
    }

    @Test
    void cell() {
        // The new Cell is within the board
        assertDoesNotThrow(() -> {
            // The new Cell is within the board
            cell = new Cell(0, 6, board);
        });
        // The new Cell is not within the board
        assertThrows(OutOfBoundsException.class, () -> {
            cell = new Cell(0, 5, board);
            cell = new Cell(-1, 6, board);
        });
    }

    @Test
    void assing() {
        assertDoesNotThrow(() -> {
            // The new Cell is within the board
            cell = new Cell(0, 6, board);
        });
        // The cell is within the board
        assertDoesNotThrow(() -> {
            cell.assign(Board.Color.Red);
        });
    }

    @Test
    void getNeighbors() {
        // The new Cell is within the board
        assertDoesNotThrow(() -> {
            cell = new Cell(0, 6, board);
        });
        // The left cell of this cell is not within the board
        assertThrows(OutOfBoundsException.class, () -> {
            cell.getLeft();
        });
        // The right cell of this cell is not within the board
        assertThrows(OutOfBoundsException.class, () -> {
            cell.getRight();
        });
        // The left cell of this cell is not within the board
        assertThrows(OutOfBoundsException.class, () -> {
            cell.getLeft();
        });
        // Nor the upper cells
        assertThrows(OutOfBoundsException.class, () -> {
            cell.getUpperLeft();
            cell.getUpperRight();
        });
        // The lower cells of this cell are within the board
        assertDoesNotThrow(() -> {
            cell.getLowerLeft();
            cell.getLowerRight();
        });
        // The new Cell is within the board
        assertDoesNotThrow(() -> {
            cell = new Cell(6, 6, board);
        });

        assertDoesNotThrow(() -> {
            cell.getRight();
            cell.getLowerRight();
            cell.getLowerLeft();
            cell.getLeft();
            cell.getUpperLeft();
            cell.getUpperRight();
        });

    }
}

