package logic;

import java.util.HashSet;

import exceptions.OutOfBoundsException;

//import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.emory.mathcs.backport.java.util.Collections;

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

    // No podemos usar Cell porque Cell usa Board
    // Primer [] posicion (Up, Down...), segundo [] celda en lista, tercer [] 0 row,
    // 1 column
    private Color[][] mat = new Color[NUM_ROW][NUM_COL];

    public Board() {
        for (int i = 0; i < NUM_ROW; ++i) {
            for (int j = 0; j < NUM_COL; ++j) {
                switch (SCHEMATIC.charAt(i * NUM_COL + j)) {
                    case 'D':
                        Side.Down.addSideCells(new Cell(i, j));
                        break;
                    case 'l':
                        Side.DownLeft.addSideCells(new Cell(i, j));
                        break;
                    case 'L':
                        Side.UpLeft.addSideCells(new Cell(i, j));
                        break;
                    case 'U':
                        Side.Up.addSideCells(new Cell(i, j));
                        break;
                    case 'R':
                        Side.UpRight.addSideCells(new Cell(i, j));
                        break;
                    case 'r':
                        Side.DownRight.addSideCells(new Cell(i, j));
                        break;
                }
                mat[i][j] = (SCHEMATIC.charAt(i * NUM_COL + j) == '.' ? Color.NotBoard : Color.Void);
            }
        }
    }
    
    //Clases anidadas

    public enum Color { // TODO: Consider renaming Color to Token or similar
        Void, // 0
        Green, // 1
        Yellow, // 2
        Orange, // 3
        Red, // 4
        Purple, // 5
        Blue, // 6
        NotBoard; // 7
    }

    public enum Side {
        Down(0, new HashSet<Cell>()), // 0
        DownLeft(1, new HashSet<Cell>()), // 1
        UpLeft(2, new HashSet<Cell>()), // 2
        Up(3, new HashSet<Cell>()), // 3
        UpRight(4, new HashSet<Cell>()), // 4
        DownRight(5, new HashSet<Cell>()); // 5

        private final int value;
        private HashSet<Cell> sideCells;

        /* Constructor */

        private Side(int value, HashSet<Cell> sideCells) {
            this.value = value;
            this.sideCells = sideCells;
        }

        /* Getters */

        public Side getOpposite() {
            switch (this) {
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

        /* Metodos */

        public int getValue() {
            return value;
        }

        /**
         * @return Devuelve las celdas que corresponden al lado del tablero(las que
         * hacen esquina)
         */

        public HashSet<Cell> getSideCells() {
            return sideCells;
        }

        /**
         * @return Deuelve las celdas opuestas a la del lado donde nos encontramos
         */
        public HashSet<Cell> getOpposingCells() {
        	
            return getOpposite().getSideCells();
        }

        /**
         * @param newCell La celda que queremos anadir al lado
         */
        private void addSideCells(Cell newCell) {
            sideCells.add(newCell);
        }
    }
    
    
    /*Métodos*/
    

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

    // CHECK: He añadido una n al nombre anterior, validRowColumn
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
