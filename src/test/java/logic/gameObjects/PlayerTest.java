package logic.gameObjects;

import static org.junit.jupiter.api.Assertions.*;

import exceptions.OccupiedCellException;
import java.awt.Color;
import logic.Board;
import logic.Cell;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.Mode;

public class PlayerTest {
    Player test;
    Board board;

    @BeforeEach
    void init() {
        try {
            board = new Board();
            test = new Player(Color.BLUE, Board.Side.Down, Mode.Traditional);
        } catch (OccupiedCellException e) {
            fail("Player start Cell previously occupied");
        }
    }

    @Test
    void selectedPiece() throws Exception {
        assertFalse(test.hasSelectedPiece());

        assertFalse(
            test.selectPiece(
                new Piece(new Cell(6, 6, board), Color.BLUE, Mode.Traditional)
            )
        );

        assertFalse(test.hasSelectedPiece());

        assertTrue(test.selectPiece(board.getCell(15, 6).getPiece()));

        assertTrue(test.hasSelectedPiece());

        test.deselectPiece();

        assertFalse(test.hasSelectedPiece());
    }
}
