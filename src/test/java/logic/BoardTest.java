package logic;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardTest {
    private Board board;

    @BeforeEach
    void init() {
        this.board = new Board();
    }

    @Test
    void insideBoard() {
        /// No validas por estar fuera del limite físico del tablero
        assertFalse(board.insideBoard(-1, 0));
        assertFalse(board.insideBoard(0, 14));

        /// No valida porque está fuera del tablero del juego
        assertFalse(board.insideBoard(0, 0));

        /// Es valida porque es una posición dentro del tablero
        assertTrue(board.insideBoard(0, 6));
    }

    @Test
    void getCell() {
        /// Devuelve null porque no es una posición del tablero
        assertNull(board.getCell(0, 0));
        assertNull(board.getCell(0, 5));
        assertNull(board.getCell(13, -1));

        /// Dentro del tablero, devuelve la referencia al Cell
        assertNotNull(board.getCell(0, 6));
    }
}
