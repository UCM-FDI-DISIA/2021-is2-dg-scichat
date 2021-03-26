package logic.gameObjects;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import exceptions.OccupiedCellException;
import logic.Board;
import logic.Cell;
import logic.Color;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

public class PlayerTest {

	Player test;
	Board board;
	
	@BeforeEach
    void init() {
        try {
        	board=new Board();
            test=new Player(Color.Blue, Board.Side.Down);
        } catch (OccupiedCellException e) {
            fail("Player start Cell previously occupied");
        }
    }
	
	@Test
	void selectedPiece() throws Exception {
		
		assertFalse(test.hasSelectedPiece());
		
		assertFalse(test.selectPiece(new Piece(new Cell(6,6,board),Color.Blue)));
		
		assertFalse(test.hasSelectedPiece());
		
		assertTrue(test.selectPiece(board.getCell(15, 6).getPiece()));
		
		assertTrue(test.hasSelectedPiece());
		
		test.deselectPiece();
		
		assertFalse(test.hasSelectedPiece());
		
	}

}
