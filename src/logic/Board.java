package logic;

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

    public Board(){
        for(int i = 0; i < NUM_ROW; ++i){
            for(int j = 0; j < NUM_COL; ++i){
                mat[i][j] = (SCHEMATIC.charAt(i*NUM_COL + j) == '.'?  Color.NotBoard : Color.Void);
            }
        }
    }

    public enum Color{ // TODO: Consider renaming Color to Token or similar
        Void,		// 0
        Green,		// 1
        Yellow,		// 2
        Orange,		// 3
        Red,		// 4
        Purple,		// 5
        Blue,		// 6
        NotBoard;	// 7
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

    public String toString(){
        String answer = "";
        for(int i = 0; i < NUM_ROW;++i){
            for(int j = 0; j<NUM_COL; ++i){
                switch(mat[i][j]){
                    case Color.NotBoard:
                        answer+=" ";
                        break;
                    case Color.Void:
                        answer+="*";
                        break;
                    case Color.Orange:
                        answer+="O";
                        break;
                    case Color.Blue:
                        answer+="B";
                        break;
                    case Color.Green:
                        answer+="G";
                        break;
                    case Color.Red:
                        answer+="R";
                        break;
                    case Color.Yellow:
                        answer+="Y";
                        break;
                    default:
                        answer+="P";
                }
            }
            answer+="\n";
        }
        return answer;
    }
    
    // CHECK:  He añadido una n al nombre anterior, validRowColumn
    public boolean validRowColum(int row, int colum){
        if(row < 0 || row >= 17 || colum < 0 || colum >= 13)
            return false;
        return mat[colum][row] != Color.NotBoard;
    }

    public boolean remove(int row, int col){
        if(!validRowColum(row,col))
            return false;
        if(available(row,colum))
            return false;
        mat[col][row] = Color.Void;
        return true;
    }

    public boolean put(int row, int colum, Color color){
        if(!validRowColum(row,col))
            return false;
        if(!available(row,colum))
            return false;
        mat[col][row] = color;
        return true;
    }

    public boolean available(int row, int col){
        return mat[row][col] == Color.Void;
    }

    //Mas vale que os ocupeis de que la casilla vale
    // public Color get();
}
