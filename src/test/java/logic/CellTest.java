package logic;

import exceptions.InvalidOperationException;
import exceptions.OutOfBoundsException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;

public class CellTest {

    private Cell cell, other;
    private Board board;
    private int times; // Perdón por llenar esto de atributos

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
    void assign() {
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
    

    @Test
    void getCellByRepeatingActions() {
    	Assertions.assertDoesNotThrow(() -> {
    		cell = new Cell(8,6,board); // Deberia ser la celda del medio
    	});
    	other = cell;
    	
    	times = 4;
    	Assertions.assertDoesNotThrow(() -> {
	    	for(int i = 0; i < times; ++i) {
	    		other = other.getUpperLeft();
	    	}
    	});
    	Assertions.assertDoesNotThrow(() -> {
    		cell = cell.getUpperLeft(times);
    	});
    	if( other.equals(cell) ) System.out.println("GOod job");
    	Assertions.assertDoesNotThrow(() -> {
    		Assertions.assertEquals(other,cell);
    	});
    	
    	times = 3;
    	Assertions.assertDoesNotThrow(() -> {
    		for(int i = 0; i < times; ++i) {
    			other = other.getRight();
    		}
    	});
    	Assertions.assertDoesNotThrow(() -> {
    		cell = cell.getRight(times);
    	});
    	Assertions.assertEquals(other,cell);
    	
    	times = 8;
    	Assertions.assertDoesNotThrow(() -> {
    		for(int i = 0; i < times; ++i) {
    			other = other.getLowerLeft();
    		}
    	});
    	Assertions.assertDoesNotThrow(() -> {
    		cell = cell.getLowerLeft(times);
    	});
    	Assertions.assertEquals(other,cell);
    	
    	times = 2;
    	Assertions.assertDoesNotThrow(() -> {
    		for(int i = 0; i < times; ++i) {
    			other = other.getLeft();
    		}
    	});
    	Assertions.assertDoesNotThrow(() -> {
    		cell = cell.getLeft(times);
    	});
    	Assertions.assertEquals(other,cell);
    	
      	times = 7;
    	Assertions.assertDoesNotThrow(() -> {
    		for(int i = 0; i < times; ++i) {
    			other = other.getUpperRight();
    		}
    	});
    	Assertions.assertDoesNotThrow(() -> {
    		cell = cell.getUpperRight(times);
    	});
    	Assertions.assertEquals(other,cell);
    }
    
    @Test
    void isInSameDiagonalAsCell() {
    	Assertions.assertDoesNotThrow(() -> {
    		cell = new Cell(8,6,board); // Deberia ser la celda del medio
    		other = cell;
    	});
    	Assertions.assertTrue(cell.isInSameDiagonalAs(other));
    	
    	for(Cell.Direction dir : Cell.Direction.values()) {
    		Assertions.assertDoesNotThrow(() -> {
    			other = cell.getByDirection(dir,3);    			
    		});
        	Assertions.assertTrue(cell.isInSameDiagonalAs(other));
        	
        	Assertions.assertDoesNotThrow(() -> {
        		other = other.getUpperRight().getLowerRight().getUpperRight(); // Me aseguro de sacarlo de la diagonal/horizontal
        	});
        	Assertions.assertFalse(cell.isInSameDiagonalAs(other));
    	}
    }
    
    Cell.Direction actualDirection;
    @Test
    void getDirectionTowardsCell() {
    	Assertions.assertDoesNotThrow(() -> {
    		cell = new Cell(8,6,board); // Deberia ser la celda del medio
    		other = cell;
    	});
    	for(Cell.Direction direction : Cell.Direction.values()) {
    		Assertions.assertDoesNotThrow(() -> {
    			other = cell.getByDirection(direction,3);    			
    		});
    		Assertions.assertDoesNotThrow(() -> {
    			actualDirection = cell.getDirectionTowards(other);
    		});
        	Assertions.assertEquals(direction, actualDirection);
    	}
    }
}

