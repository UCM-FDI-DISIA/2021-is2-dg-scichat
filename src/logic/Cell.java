package logic;

import logic.Board;

public class Cell{
    private int row, col;
    private static Board board;

    public Cell(int row, int col, Board board) throws OutOfBoundsException{
        if(!validRowColumn(row,col)) 
            throw new OutOfBoundException(row,col);
        this.row = row;
        this.col = col;
        this.board = board;
    }
    
    public getRow(){ return row; }
    public getCol(){ return col; }

    public void assign(Board.Color = color){
        //TODO:
    }

    //TODO: Implement getNeighbours() → Lista{UL,UR,R,LR,LL,L}
    public Cell getUpperRight(){  
        if(row%2 == 1) // Fila impar
            return new Cell(row-1,col,board);
        else  // Fila par
            return new Cell(row-1,col+1,board);
    }

    public Cell getUpperLeft(){
        if(row%2 == 1) // Fila impar
            return new Cell(row-1,col-1,board);
        else  // Fila par
            return new Cell(row-1,col,board);
    }

    public Cell getLowerRight(){ 
        if(row%2 == 1) // Fila impar
            return new Cell(row+1,col,board);
        else  // Fila par
            return new Cell(row+1,col+1,board);
    }

    public Cell getLowerLeft(){
        if(row%2 == 1) // Fila impar
            return new Cell(row+1,col-1,board);
        else  // Fila par
            return new Cell(row+1,col,board);
    }

    public Cell getRight(){ 
        return new Cell(row,col+1,board);
    }
    
    public Cell getLeft(){ 
        return new Cell(row,col-1,board);
    }
}