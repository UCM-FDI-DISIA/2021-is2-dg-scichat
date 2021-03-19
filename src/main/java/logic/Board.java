package logic;

import java.util.HashSet;

import exceptions.OutOfBoundsException;

public class Board {
    private static final int NUM_COL = 13, NUM_ROW = 17;
    private static final String SCHEMATIC = "......U......" +
            "......UU....." +
            ".....UUU....." +
            ".....UUUU...." +
            "LLLL@@@@@RRRR" +
            ".LLL@@@@@@RRR" +
            ".LL@@@@@@@RR." +
            "..L@@@@@@@@R." +
            "..@@@@@@@@@.." +
            "..l@@@@@@@@r." +
            ".ll@@@@@@@rr." +
            ".lll@@@@@@rrr" +
            "llll@@@@@rrrr" +
            ".....DDDD...." +
            ".....DDD....." +
            "......DD....." +
            "......D......";

    //No podemos usar Cell porque Cell usa Board
    //Primer [] posicion (Up, Down...), segundo [] celda en lista, tercer [] 0 row, 1 column
    private int[][][] zone= new int[6][10][2];
    private Color[][] mat = new Color[NUM_ROW][NUM_COL];

    public Board() {
    	int[] nzona=new int[6];
        for (int i = 0; i < NUM_ROW; ++i) {
            for (int j = 0; j < NUM_COL; ++j) {
            	int pos=-1;
            	switch(SCHEMATIC.charAt(i * NUM_COL + j)) {
            		case 'D':
            			pos=Sides.Down.getValue();
            			break;
            		case 'l':
            			pos=Sides.DownLeft.getValue();
            			break;
            		case 'L':
            			pos=Sides.UpLeft.getValue();
            			break;
            		case 'U':
            			pos=Sides.Up.getValue();
            			break;
            		case 'R':
            			pos=Sides.UpRight.getValue();
            			break;
            		case 'r':
            			pos=Sides.DownRight.getValue();
            			break;
            	}
                mat[i][j] = (SCHEMATIC.charAt(i * NUM_COL + j) == '.' ? Color.NotBoard : Color.Void);
                if(pos!=-1) {
                	zone[pos][nzona[pos]][0]=i;
                	zone[pos][nzona[pos]][1]=j;
                	nzona[pos]+=1;
                }
            }
        }
    }

    public enum Color { // TODO: Consider renaming Color to Token or similar
        Void,       // 0
        Green,      // 1
        Yellow,     // 2
        Orange,     // 3
        Red,        // 4
        Purple,     // 5
        Blue,       // 6
        NotBoard;   // 7
    }
    
    public enum Sides{
    	Down(0),		//0
    	DownLeft(1),	//1
    	UpLeft(2),		//2
    	Up(3),			//3
    	UpRight(4),		//4
    	DownRight(5);	//5
    	
    	private final int value;
    	private Sides(int value) {
            this.value = value;
        }
    	public int getValue() {
            return value;
        }
    }
    
    //Esto devolvera un array con los Cell respectivos del lado que des
    public HashSet<Cell> getPointsOnSide(Sides side) throws OutOfBoundsException{
    	HashSet<Cell> out=new HashSet<Cell>();
    	for(int i=0;i<10;i++) {
    		out.add(new Cell(zone[side.getValue()][i][0], zone[side.getValue()][i][1], this));
    	}
		return out;
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
