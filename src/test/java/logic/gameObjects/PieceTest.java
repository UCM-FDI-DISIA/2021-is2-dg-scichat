package logic.gameObjects;

import exceptions.InvalidMoveException;
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
    Piece piece, piece2, piece3;
    Cell posAux = null;
    Board board = null;
    Color color = null;

    @BeforeEach
    void init() {
        try {
            board = new Board();
            color = Color.Blue;
            piece = new Piece(new Cell(8, 6, board), color);
            piece2 = new Piece(piece.getPosition().getUpperLeft(), color);
            piece3 = new Piece(piece.getPosition().getUpperLeft().getUpperLeft().getUpperRight(), color);
            
        } catch (OccupiedCellException e) {
            fail("Middle cell wasn't empty in initialization.");
        }
    }

    @Test
    void tryToMoveToCell() { // TODO: Expand in all directions, more movement
      Assertions.assertDoesNotThrow(() -> {
            posAux = piece.getPosition();
        });

        Assertions.assertThrows(InvalidMoveException.class, () -> {
            piece.tryToMoveTo(posAux);
        });

        Assertions.assertDoesNotThrow(() -> {
            posAux = posAux.getUpperLeft();

            piece.tryToMoveTo(posAux);
        });

      Assertions.assertDoesNotThrow(() -> {
            System.out.println("Pieza de la que partimos " + piece.getPosition());
            System.out.println("Pieza que queremos saltar: " + piece2.getPosition());
            System.out.println("Pieza a la que queremos llegar" + piece2.getPosition().getUpperLeft());
            piece.tryToMoveTo(piece2.getPosition().getUpperLeft());
        }); 
        
        Assertions.assertDoesNotThrow(() -> {
            System.out.println("Pieza de la que partimos " + piece.getPosition());
            System.out.println("Pieza que queeremos saltar primero: " + piece2.getPosition());
            System.out.println("Pieza que queremos saltar después: " + piece3.getPosition());
            System.out.println("Pieza a la que queremos llegar" + piece3.getPosition().getUpperRight());
            piece.tryToMoveTo(piece3.getPosition().getUpperRight());
        });
        
        
        Assertions.assertThrows(InvalidMoveException.class, () -> {
            piece.tryToMoveTo(piece3.getPosition());
        });

       Assertions.assertThrows(InvalidMoveException.class, () -> {
            piece.tryToMoveTo(piece2.getPosition().getLowerLeft(2));
        });
    }

}
