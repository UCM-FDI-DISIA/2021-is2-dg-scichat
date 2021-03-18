package logic;

import exceptions.OutOfBoundsException;
import java.util.HashSet;

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
            			Side.Down.addSideCells(new Cell(i, j));
            			pos=Side.Down.getValue();
            			break;
            		case 'l':
            			Side.DownLeft.addSideCells(new Cell(i, j));
            			pos=Side.DownLeft.getValue();
            			break;
            		case 'L':
            			Side.UpLeft.addSideCells(new Cell(i, j));
            			pos=Side.UpLeft.getValue();
            			break;
            		case 'U':
            			Side.Up.addSideCells(new Cell(i, j));
            			pos=Side.Up.getValue();
            			break;
            		case 'R':
            			Side.UpRight.addSideCells(new Cell(i, j));
            			pos=Side.UpRight.getValue();
            			break;
            		case 'r':
            			Side.DownRight.addSideCells(new Cell(i, j));
            			pos=Side.DownRight.getValue();
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
    
    public enum Side{
    	Down(0, new HashSet<Cell>()),		//0
    	DownLeft(1, new HashSet<Cell>()),	//1
    	UpLeft(2, new HashSet<Cell>()),		//2
    	Up(3, new HashSet<Cell>()),			//3
    	UpRight(4, new HashSet<Cell>()),		//4
    	DownRight(5, new HashSet<Cell>());	//5
    	
    	private final int value;
    	private HashSet<Cell> sideCells;
    	
    	/*Constructor*/
    	
    	private Side(int value, HashSet<Cell> sideCells) {
            this.value = value;
            this.sideCells = sideCells;
        }
    	
    	/*Getters*/
    	
    	public Side getOpposite() {
    		switch(this) {
    		case Down:
    			return Up;
    		case Up:
    			return Down;
    		case UpLeft:
    			return DownRight;
    		case UpRight:
    			return DownLeft;
    		case DownRight:
    			return UpLeft;
    		case DownLeft:
    			return UpRight;
    		default:
    			return null;
    		}
    	}
    
    
  	
    	/*Metodos*/
    	
    	public int getValue() {
            return value;
        }
    	/**
    	 * 
    	 * @return	Devuelve las celdas que corresponden al lado del tablero(las que hacen esquina)
    	 */
    	
    	public HashSet<Cell> getSideCells(){
    		return sideCells;
    	}
    	/**
    	 * 
    	 * @return	Deuelve las celdas opuestas a la del lado donde nos encontramos
    	 */
    	public HashSet<Cell> getOpposeCells() {
    		return getOpposite().getSideCells();
    	}
    	/**
    	 * 
    	 * @param newCell La celda que queremos anadir
    	 */
    	private void addSideCells(Cell newCell) {
    		sideCells.add(newCell);
    	}
    }
    
    //Esto devolvera un array con los Cell respectivos del lado que des
    public Cell[] getPointsOnSide(Side side) throws OutOfBoundsException{
    	Cell[] out=new Cell[10];
    	for(int i=0;i<10;i++) {
    		out[i]=new Cell(zone[side.getValue()][i][0], zone[side.getValue()][i][1], this);
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
