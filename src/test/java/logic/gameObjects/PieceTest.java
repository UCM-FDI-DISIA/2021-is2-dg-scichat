package logic.gameObjects;

import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import logic.Board;
import logic.Cell;
import logic.Color;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

class PieceTest {
    Piece piece, piece2;
    Cell pos = null;
    Board board = null;
    Color color = null;

    @BeforeEach
    void init() {
        try {
            board = new Board();
            color = Color.Blue;
            piece = new Piece(new Cell(8, 6, board), color);
        } catch (OccupiedCellException e) {
            fail("Middle cell wasn't empty in initialization.");
        }
    }

    @Test
    void tryToMoveToCell() { // TODO: Expand in all directions, more movement
        Assertions.assertDoesNotThrow(() -> {
            pos = piece.getPosition();
        });

        Assertions.assertThrows(InvalidOperationException.class, () -> {
            piece.tryToMoveTo(pos);
        });

        Assertions.assertDoesNotThrow(() -> {
            pos = pos.getUpperLeft();
            piece.tryToMoveTo(pos);
        });

        Assertions.assertDoesNotThrow(() -> {
            piece2 = new Piece(piece.getPosition().getUpperRight(), color);
            piece.tryToMoveTo(piece2.getPosition().getUpperRight());
        });

        Assertions.assertThrows(InvalidOperationException.class, () -> {
            piece.tryToMoveTo(piece2.getPosition().getUpperLeft(2));
        });

        // TODO: Check for invalid jumps
    }


}
