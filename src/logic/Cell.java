package logic;

import exceptions.OutOfBoundsException;
import logic.Board;
import java.util.ArrayList;
import java.util.Collections;

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
        this.board.put(row,col,color);
    }
    public void remove(){
        this.board.remove(row,col);
    }

    public List<Cell> getNeighbours() {
        // Devuelve las celdas, donde 0 es R, y siguen en el sentido de 
        // las agujas del reloj
        List<Cell> ret = new ArrayList<Cell>();
        ret.add(this.getRight());
        ret.add(this.getLowerRight());
        ret.add(this.getLowerLeft());
        ret.add(this.getLeft());
        ret.add(this.getUpperLeft());
        ret.add(this.getUpperRight());
        return Collections.unmodifiableList(ret);
    }

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