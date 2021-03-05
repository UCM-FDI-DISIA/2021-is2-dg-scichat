package logic;

import exceptions.OutOfBoundsException;
import logic.Board;

public class Cell{
    private int row, col;
    private static Board board; // TODO: Lo dejamos como estático? Y si queremos tener varias instancias del juego, o varios tableros?

    public Cell(int row, int col, Board board) throws OutOfBoundsException{
        if(!board.validRowColumn(row,col)) 
            throw new OutOfBoundsException(row,col);
        this.row = row;
        this.col = col;
        Cell.board = board;
    }
    
    public int getRow(){ return row; }
    public int getCol(){ return col; }

    public void assign(Board.Color color){
        //TODO: Completar o repensar (A lo mejor no queremos un assign, sino un place y un
    	//		empty para poner un color y vaciar una ficha (Poner Void)
    }

    //TODO: Implement getNeighbours() → Lista{UL,UR,R,LR,LL,L}
    public Cell getUpperRight() throws OutOfBoundsException{  
        if(row%2 == 1) // Fila impar
            return new Cell(row-1,col,board);
        else  // Fila par
            return new Cell(row-1,col+1,board);
    }

    public Cell getUpperLeft() throws OutOfBoundsException{
        if(row%2 == 1) // Fila impar
            return new Cell(row-1,col-1,board);
        else  // Fila par
            return new Cell(row-1,col,board);
    }

    public Cell getLowerRight() throws OutOfBoundsException{ 
        if(row%2 == 1) // Fila impar
            return new Cell(row+1,col,board);
        else  // Fila par
            return new Cell(row+1,col+1,board);
    }

    public Cell getLowerLeft() throws OutOfBoundsException{
        if(row%2 == 1) // Fila impar
            return new Cell(row+1,col-1,board);
        else  // Fila par
            return new Cell(row+1,col,board);
    }

    public Cell getRight() throws OutOfBoundsException{ 
        return new Cell(row,col+1,board);
    }
    
    public Cell getLeft() throws OutOfBoundsException{ 
        return new Cell(row,col-1,board);
    }
}