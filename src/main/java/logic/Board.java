package logic;

import exceptions.OutOfBoundsException;

public class Board {
    private static final int NUM_COL = 13, NUM_ROW = 17;
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


    private Color[][] mat = new Color[NUM_ROW][NUM_COL];

    public Board() {
        for (int i = 0; i < NUM_ROW; ++i) {
            for (int j = 0; j < NUM_COL; ++j) {
                mat[i][j] = (SCHEMATIC.charAt(i * NUM_COL + j) == '.' ? Color.NotBoard : Color.Void);
            }
        }
    }

    public enum Color { // TODO: Consider renaming Color to Token or similar
        Void,        // 0
        Green,        // 1
        Yellow,        // 2
        Orange,        // 3
        Red,        // 4
        Purple,        // 5
        Blue,        // 6
        NotBoard;    // 7
    }
    
    public enum Sides{
    	Down,		//0
    	DownLeft,	//1
    	UpLeft,		//2
    	Up,			//3
    	UpRight,	//4
    	DownRight;	//5
    }
    
    //Esto devolvera un array con los Cell respectivos del lado que des
    public Cell[] getPointsOnSide(Sides side) {
		return null;
    }

    /*public boolean validRowColum(int row, int colum){
        return (row < 4 && colum <= 7 + row/2 && colum >= 7 - (row-1)/2 ) || (row > 12 && colum <= 7 + (16-row)/2 && colum >= 7 - (15-row)/2 ) || ( row >= 4 && row <= 8 && colum >= (row-3)/2 && colum >= 12 - (row-4)/2) && ( row <=  );
    }*/
    
    /*public void initializeTable(int numPlayers){
        if(numPlayers < 2 || numPlayers > 6){
            switch(numPlayers){
                case(1):
                    initializeForOne();
                    break;
                case(2):
                    initializeForTwo();
                    break;
                case(3):
                    initializeForThree();
                    break;
                case(4):
                    initializeForFour();
                case(5):
                    initializeForFive();
                    break;
                case(6):
                    initializeForSix();
            }
        }
        else{
            throw new TooManyPlayersException();
        }    
    }

    private void initializeForTwo(){
        for(int i = 0; i < 4; ++i){
            for(Color aux : mat[i]){
                if(aux != Void)
                    aux = 
            }
        }
    }

    private void initializeForThree(){}

    private void initializeForFour(){}

    private void initializeForFive(){}

    private void initializeForSix(){}
    */

    public String toString() {
        String answer = "";
        for (int i = 0; i < NUM_ROW; ++i) {
            for (int j = 0; j < NUM_COL; ++j) {
                switch (mat[i][j]) {
                    case NotBoard:
                        answer += " ";
                        break;
                    case Void:
                        answer += "*";
                        break;
                    case Orange:
                        answer += "O";
                        break;
                    case Blue:
                        answer += "B";
                        break;
                    case Green:
                        answer += "G";
                        break;
                    case Red:
                        answer += "R";
                        break;
                    case Yellow:
                        answer += "Y";
                        break;
                    default:
                        answer += "P";
                }
            }
            answer += "\n";
        }
        return answer;
    }

    // CHECK:  He añadido una n al nombre anterior, validRowColumn
    public boolean validRowColumn(int row, int col) {
        if (!insideBoard(row, col))
            return false;
        return mat[row][col] != Color.NotBoard;
    }

    public boolean remove(int row, int col) {
        if (!validRowColumn(row, col))
            return false;
        if (available(row, col))
            return false;
        mat[row][col] = Color.Void;
        return true;
    }

    public boolean put(int row, int col, Color color) throws OutOfBoundsException {
        if (!insideBoard(row, col))
            throw new OutOfBoundsException();
        if (!validRowColumn(row, col))
            return false;
        if (!available(row, col))
            return false;
        mat[row][col] = color;
        return true;
    }

    public boolean available(int row, int col) {
        return mat[row][col] == Color.Void;
    }

    public Color get(int row, int col) throws OutOfBoundsException {
        if (!insideBoard(row, col))
            throw new OutOfBoundsException();
        return mat[row][col];
    }

    public boolean insideBoard(int row, int col) {
        return (0 <= row && row < NUM_ROW) && (0 <= col && col < NUM_COL);
    }
}
