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

    @Test
    void forEach() {
        String[] eo = {
            "(0,6)",
            "(1,6)",
            "(1,7)",
            "(2,5)",
            "(2,6)",
            "(2,7)",
            "(3,5)",
            "(3,6)",
            "(3,7)",
            "(3,8)",
            "(4,0)",
            "(4,1)",
            "(4,2)",
            "(4,3)",
            "(4,4)",
            "(4,5)",
            "(4,6)",
            "(4,7)",
            "(4,8)",
            "(4,9)",
            "(4,10)",
            "(4,11)",
            "(4,12)",
            "(5,1)",
            "(5,2)",
            "(5,3)",
            "(5,4)",
            "(5,5)",
            "(5,6)",
            "(5,7)",
            "(5,8)",
            "(5,9)",
            "(5,10)",
            "(5,11)",
            "(5,12)",
            "(6,1)",
            "(6,2)",
            "(6,3)",
            "(6,4)",
            "(6,5)",
            "(6,6)",
            "(6,7)",
            "(6,8)",
            "(6,9)",
            "(6,10)",
            "(6,11)",
            "(7,2)",
            "(7,3)",
            "(7,4)",
            "(7,5)",
            "(7,6)",
            "(7,7)",
            "(7,8)",
            "(7,9)",
            "(7,10)",
            "(7,11)",
            "(8,2)",
            "(8,3)",
            "(8,4)",
            "(8,5)",
            "(8,6)",
            "(8,7)",
            "(8,8)",
            "(8,9)",
            "(8,10)",
            "(9,2)",
            "(9,3)",
            "(9,4)",
            "(9,5)",
            "(9,6)",
            "(9,7)",
            "(9,8)",
            "(9,9)",
            "(9,10)",
            "(9,11)",
            "(10,1)",
            "(10,2)",
            "(10,3)",
            "(10,4)",
            "(10,5)",
            "(10,6)",
            "(10,7)",
            "(10,8)",
            "(10,9)",
            "(10,10)",
            "(10,11)",
            "(11,1)",
            "(11,2)",
            "(11,3)",
            "(11,4)",
            "(11,5)",
            "(11,6)",
            "(11,7)",
            "(11,8)",
            "(11,9)",
            "(11,10)",
            "(11,11)",
            "(11,12)",
            "(12,0)",
            "(12,1)",
            "(12,2)",
            "(12,3)",
            "(12,4)",
            "(12,5)",
            "(12,6)",
            "(12,7)",
            "(12,8)",
            "(12,9)",
            "(12,10)",
            "(12,11)",
            "(12,12)",
            "(13,5)",
            "(13,6)",
            "(13,7)",
            "(13,8)",
            "(14,5)",
            "(14,6)",
            "(14,7)",
            "(15,6)",
            "(15,7)",
            "(16,6)"
        };
        int idx = 0;
        for (Cell cell : board) {
            assertEquals(cell.toString(), eo[idx]);
            idx++;
        }
    }
}	
