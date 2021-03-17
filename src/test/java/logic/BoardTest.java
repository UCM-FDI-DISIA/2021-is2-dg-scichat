package logic;

import exceptions.OutOfBoundsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    private Board board;

    @BeforeEach
    void init() {
        this.board = new Board();
    }

    @Test
    void validRowColumn() {
        /// No validas por estar fuera del limite físico del tablero
        assertFalse(board.validRowColumn(-1, 0));
        assertFalse(board.validRowColumn(0, 14));

        /// No valida porque está fuera del tablero del juego
        assertFalse(board.validRowColumn(0, 0));

        /// Es valida porque es una posición dentro del tablero
        assertTrue(board.validRowColumn(0, 6));
    }

    @Test
    void remove() {
        /// No hay pieza inicialmente en (0, 6)
        assertDoesNotThrow(() -> {
            assertEquals(Board.Color.Void, board.get(0, 6));
        });

        /// Entonces fracasa en la operación de remove
        assertFalse(board.remove(0, 6));

        /// Poner pieza roja en (0, 6)
        assertDoesNotThrow(() -> {
            assertTrue(board.put(0, 6, Board.Color.Red));
        });

        /// Ahora tiene que devolver true al intentar quitar esa pieza
        assertTrue(board.remove(0, 6));
    }

    @Test
    void put() {
        assertDoesNotThrow(() -> {
            /// No tiene que lanzar excepción, porque está accediendo a posición válida
            assertEquals(Board.Color.Void, board.get(0, 6));
        });

        /// Poner pieza roja en (0, 6)
        assertDoesNotThrow(() -> {
            assertTrue(board.put(0, 6, Board.Color.Red));
        });

        assertDoesNotThrow(() -> {
            /// Y tiene que devolver la pieza roja que acabamos de introducir
            assertEquals(Board.Color.Red, board.get(0, 6));
        });

        assertThrows(OutOfBoundsException.class, () -> {
            /// Tiene que lanzar excepción por poner en una posición fuera del tablero físico
            board.put(-1, 0, Board.Color.Red);
        });

        assertDoesNotThrow(() -> {
            /// No lanza excepción, simplemente devuelve false si intenta poner ficha en una posición inválida del tablero
            assertFalse(board.put(0, 0, Board.Color.Red));
        });
    }

    @Test
    void available() {
        /// Esta disponible porque no hemos puesto ninguna pieza
        assertTrue(board.available(0, 6));

        assertDoesNotThrow(() -> {
            assertTrue(board.put(0, 6, Board.Color.Red));
        });

        /// Ahora ya no esta disponible
        assertFalse(board.available(0, 6));
    }
    
}