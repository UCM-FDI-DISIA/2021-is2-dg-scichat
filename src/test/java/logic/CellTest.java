package logic;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import exceptions.OutOfBoundsException;
import logic.Board.Side;
import logic.gameObjects.Piece;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.PieceColor;

public class CellTest {
    private Cell cell, other;
    private Board board;
    private int times; // Perdón por llenar esto de atributos

    @BeforeEach
    void init() {
        /// Crear un tablero antes de cualquier test, porque las celdas están asociadas
        /// con la creación del tablero
        board = new Board();
    }

    @Test
    void isEmpty() {
        Cell cell = board.getCell(0, 6);

        /// Hay una celda en posición 0, 6
        assertNotNull(cell);

        /// Y está sin pieza
        assertTrue(cell.isEmpty());

        assertDoesNotThrow(
            () -> {
                /// Crear un nueva pieza, y poner en esa posición libre
                Piece piece = new Piece(cell, PieceColor.RED);
            }
        );

        /// Ya estaría ocupada ahora
        assertFalse(cell.isEmpty());
    }

    @Test
    void putPiece() {
        /// Seguir con ese test anterior
        /// Al final, la casilla (0, 6) está ocupada
        this.isEmpty();
        Cell cell = board.getCell(0, 6);

        assertThrows(
            OccupiedCellException.class,
            () -> {
                /// Intentamos poner una pieza en esa misma posición
                /// Se invoca el método putPiece desde el constructor de Piece
                new Piece(cell, PieceColor.RED);
            }
        );

        Cell cell2 = board.getCell(1, 7);
        assertNotNull(cell2);
        assertTrue(cell2.isEmpty());

        assertDoesNotThrow(
            () -> {
                /// Intentamos poner una pieza en otra posición libre
                /// Ahora no tiene que haber problema

                new Piece(cell2, PieceColor.RED);
            }
        );
    }

    @Test
    void removePiece() {
        Cell cell = board.getCell(0, 6);

        /// Hay una celda en posición 0, 6, y libre
        assertNotNull(cell);
        assertTrue(cell.isEmpty());

        assertDoesNotThrow(
            () -> {
                /// Crear un nueva pieza, y poner en esa posición libre
                new Piece(cell, PieceColor.RED);
            }
        );

        assertDoesNotThrow(
            () -> {
                /// Quitar esa pieza de la celda
                cell.removePiece();
            }
        );

        assertThrows(
            InvalidOperationException.class,
            () -> {
                /// Lanza excepción porque no hay pieza en la celda ya
                cell.removePiece();
            }
        );
    }

    /// Hay que discutir si lanza excepción en método getCell cuando se sale del
    /// tablero o no
    @Test
    @Disabled
    void getNeighbors() {
        // The new Cell is within the board
        assertDoesNotThrow(
            () -> {
                cell = new Cell(0, 6, board);
            }
        );
        // The left cell of this cell is not within the board
        assertThrows(
            OutOfBoundsException.class,
            () -> {
                cell.getLeft();
            }
        );
        // The right cell of this cell is not within the board
        assertThrows(
            OutOfBoundsException.class,
            () -> {
                cell.getRight();
            }
        );
        // The left cell of this cell is not within the board
        assertThrows(
            OutOfBoundsException.class,
            () -> {
                cell.getLeft();
            }
        );
        // Nor the upper cells
        assertThrows(
            OutOfBoundsException.class,
            () -> {
                cell.getUpperLeft();
                cell.getUpperRight();
            }
        );
        // The lower cells of this cell are within the board
        assertDoesNotThrow(
            () -> {
                cell.getLowerLeft();
                cell.getLowerRight();
            }
        );
        // The new Cell is within the board
        assertDoesNotThrow(
            () -> {
                cell = new Cell(6, 6, board);
            }
        );

        assertDoesNotThrow(
            () -> {
                cell.getRight();
                cell.getLowerRight();
                cell.getLowerLeft();
                cell.getLeft();
                cell.getUpperLeft();
                cell.getUpperRight();
            }
        );
    }

    @Test
    void getCellByRepeatingActions() {
        Assertions.assertDoesNotThrow(
            () -> {
                cell = new Cell(8, 6, board); // Deberia ser la celda del medio
            }
        );
        other = cell;

        times = 4;
        Assertions.assertDoesNotThrow(
            () -> {
                for (int i = 0; i < times; ++i) {
                    other = other.getUpperLeft();
                }
            }
        );
        Assertions.assertDoesNotThrow(
            () -> {
                cell = cell.getUpperLeft(times);
            }
        );
        if (other.equals(cell)) System.out.println("GOod job");
        Assertions.assertDoesNotThrow(
            () -> {
                Assertions.assertEquals(other, cell);
            }
        );

        times = 3;
        Assertions.assertDoesNotThrow(
            () -> {
                for (int i = 0; i < times; ++i) {
                    other = other.getRight();
                }
            }
        );
        Assertions.assertDoesNotThrow(
            () -> {
                cell = cell.getRight(times);
            }
        );
        Assertions.assertEquals(other, cell);

        times = 8;
        Assertions.assertDoesNotThrow(
            () -> {
                for (int i = 0; i < times; ++i) {
                    other = other.getLowerLeft();
                }
            }
        );
        Assertions.assertDoesNotThrow(
            () -> {
                cell = cell.getLowerLeft(times);
            }
        );
        Assertions.assertEquals(other, cell);

        times = 2;
        Assertions.assertDoesNotThrow(
            () -> {
                for (int i = 0; i < times; ++i) {
                    other = other.getLeft();
                }
            }
        );
        Assertions.assertDoesNotThrow(
            () -> {
                cell = cell.getLeft(times);
            }
        );
        Assertions.assertEquals(other, cell);

        times = 7;
        Assertions.assertDoesNotThrow(
            () -> {
                for (int i = 0; i < times; ++i) {
                    other = other.getUpperRight();
                }
            }
        );
        Assertions.assertDoesNotThrow(
            () -> {
                cell = cell.getUpperRight(times);
            }
        );
        Assertions.assertEquals(other, cell);
    }

    @Test
    void isInSameDiagonalAsCell() {
        Assertions.assertDoesNotThrow(
            () -> {
                cell = new Cell(8, 6, board); // Deberia ser la celda del medio
                other = cell;
            }
        );
        Assertions.assertTrue(cell.isInSameDiagonalAs(other));

        for (Cell.Direction dir : Cell.Direction.values()) {
            Assertions.assertDoesNotThrow(
                () -> {
                    other = cell.getByDirection(dir, 3);
                }
            );
            Assertions.assertTrue(cell.isInSameDiagonalAs(other));

            Assertions.assertDoesNotThrow(
                () -> {
                    other = other.getUpperRight().getLowerRight().getUpperRight(); // Me aseguro de sacarlo de la
                    // diagonal/horizontal
                }
            );
            Assertions.assertFalse(cell.isInSameDiagonalAs(other));
        }
    }

    Cell.Direction actualDirection;

    @Test
    void getDirectionTowardsCell() {
        Assertions.assertDoesNotThrow(
            () -> {
                cell = new Cell(8, 6, board); // Deberia ser la celda del medio
                other = cell;
            }
        );
        for (Cell.Direction direction : Cell.Direction.values()) {
            Assertions.assertDoesNotThrow(
                () -> {
                    other = cell.getByDirection(direction, 3);
                }
            );
            Assertions.assertDoesNotThrow(
                () -> {
                    actualDirection = cell.getDirectionTowards(other);
                }
            );
            Assertions.assertEquals(direction, actualDirection);
        }
    }

    @Test
    void CellToString() {
        Assertions.assertDoesNotThrow(
            () -> {
                cell = new Cell(8, 6, board); // Deberia ser la celda del medio
            }
        );

        Assertions.assertEquals("(8,6)", cell.toString());
    }

    @Test
    void getOppositeCornerCell() {
        assertEquals(board.getOppositeCornerCell(0).getRow(), 0);
        assertEquals(board.getOppositeCornerCell(0).getCol(), 6);

        assertEquals(board.getOppositeCornerCell(1).getRow(), 4);
        assertEquals(board.getOppositeCornerCell(1).getCol(), 12);

        assertEquals(board.getOppositeCornerCell(2).getRow(), 12);
        assertEquals(board.getOppositeCornerCell(2).getCol(), 12);

        assertEquals(board.getOppositeCornerCell(3).getRow(), 16);
        assertEquals(board.getOppositeCornerCell(3).getCol(), 6);

        assertEquals(board.getOppositeCornerCell(4).getRow(), 12);
        assertEquals(board.getOppositeCornerCell(4).getCol(), 0);

        assertEquals(board.getOppositeCornerCell(5).getRow(), 4);
        assertEquals(board.getOppositeCornerCell(5).getCol(), 0);
    }
}
