package logic;

public class Board {

    private static final NUM_COL = 17, NUM_ROW = 13;
    private static final String SCHEMATIC = "......@......" +
                                            "......@@....." +
                                            ".....@@@....." +
                                            ".....@@@@...." +
                                            "@@@@@@@@@@@@@" +
                                            ".@@@@@@@@@@@@" +
                                            ".@@@@@@@@@@@." +
                                            "..@@@@@@@@@@." +
                                            "..@@@@@@@@@.." +
                                            "..@@@@@@@@@@." +
                                            ".@@@@@@@@@@@." +
                                            ".@@@@@@@@@@@@" +
                                            "@@@@@@@@@@@@@" +
                                            ".....@@@@...." +
                                            ".....@@@....." +
                                            "......@@....." +
                                            "......@......";
    

    private Color mat[NUM_ROW][NUM_COL];

    public Board(){
        for(int i = 0; i < NUM_COL; ++i){
            for(int j = 0; j < NUM_ROW; ++i){
                mat[i][j] = (SCHEMATIC[i*NUM_ROW+j] == "."?  NotBoard : Void);
            }
        }
    }

    public enum Color{
        Void(0),
        Green(1),
        Yellow(2),
        Orange(3),
        Red(4),
        Purple(5),
        Blue(6),
        NotBoard(7);
    }

    /*public boolean validRowColum(int row, int colum){
        return (row < 4 && colum <= 7 + row/2 && colum >= 7 - (row-1)/2 ) || (row > 12 && colum <= 7 + (16-row)/2 && colum >= 7 - (15-row)/2 ) || ( row >= 4 && row <= 8 && colum >= (row-3)/2 && colum >= 12 - (row-4)/2) && ( row <=  );
    }*/
    
    /*public initializeTable(int numPlayers){
        if(numPlayers < 2 || numPlayers > 6){
            switch(numPlayers){
                case(1):
            }
        }
        else{
            throw new TooManyPlayersException();
        }    
    }*/
    
    public boolean validRowColum(int row, int colum){
        return mat[colum][row] != NotBoard;
    }

    //Devuelve 
    //1 si se puede mover y actualiza el tablero, 
    //0 si el movimiento es imposible, no actualiza el tablero, 
    //2 si hay una ficha en el destino, no actualiza el tablero
    public int move(int columOrigin, int rowOrigin, int columDest, int rowDest){
        if(rowOrigin < 0 || rowOrigin >= 17 || columOrigin < 0 || columOrigin >= 13)
            return 0;
        if(rowDest < 0 || rowDest >= 17 || columDest < 0 || columDest >= 13)
            return 0;
        if(mat[columOrigin][rowOrigin] == Void)
            return 0;
        if(mat[columDest][rowDest] != Void)
            return 2;
        mat[columDest][rowDest] = mat[columOrigin][rowOrigin];
        return 1;
    } 

    public boolean available(int row, int col){
        return mat[row][col] == Void;
    }

    //Mas vale que os ocupeis de que la casilla vale
    public Color get();
}

/*
SCHEMATIC
......@......
......@@.....
.....@@@.....
.....@@@@....
@@@@@@@@@@@@@
.@@@@@@@@@@@@
.@@@@@@@@@@@.
..@@@@@@@@@@.
..@@@@@@@@@..
..@@@@@@@@@@.
.@@@@@@@@@@@.
.@@@@@@@@@@@@
@@@@@@@@@@@@@
.....@@@@....
.....@@@.....
......@@.....
......@......
*/