package logic;

import java.util.HashSet;

public class Board {
    private static final int NUM_COL = 13;
    private static final int NUM_ROW = 17;

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

    /// Matriz de celdas
    private final Cell[][] cells = new Cell[NUM_ROW][NUM_COL];

    public Board() {
        for (int i = 0; i < NUM_ROW; ++i) {
            for (int j = 0; j < NUM_COL; ++j) {
                /// Comprobar si esta dentro del tablero
                boolean insideBoard = (SCHEMATIC.charAt(i * NUM_COL + j) == '.');
                Cell cell = new Cell(i, j, this, insideBoard);

                cells[i][j] = cell;

                switch (SCHEMATIC.charAt(i * NUM_COL + j)) {
                    case 'D':
                        Side.Down.addSideCells(cell);
                        break;
                    case 'l':
                        Side.DownLeft.addSideCells(cell);
                        break;
                    case 'L':
                        Side.UpLeft.addSideCells(cell);
                        break;
                    case 'U':
                        Side.Up.addSideCells(cell);
                        break;
                    case 'R':
                        Side.UpRight.addSideCells(cell);
                        break;
                    case 'r':
                        Side.DownRight.addSideCells(cell);
                        break;
                }
            }
        }
    }

    public enum Side {
        Down(0, new HashSet<>()), // 0
        DownLeft(1, new HashSet<>()), // 1
        UpLeft(2, new HashSet<>()), // 2
        Up(3, new HashSet<>()), // 3
        UpRight(4, new HashSet<>()), // 4
        DownRight(5, new HashSet<>()); // 5

        private final int value;
        private HashSet<Cell> sideCells;

        /* Constructor */
        Side(int value, HashSet<Cell> sideCells) {
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

        /* Métodos */

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
         * @param newCell La celda que queremos añadir al lado
         */
        private void addSideCells(Cell newCell) {
            sideCells.add(newCell);
        }
    }

    public String toString() {
        String result = "";
        for (int i = 0; i < NUM_ROW; ++i) {
            for (int j = 0; j < NUM_COL; ++j) {
                Cell cell = cells[i][j];

                if (!cell.insideBoard) {
                    /// No es una posición del tablero
                    result += " ";
                } else if (cell.isEmpty()) {
                    /// Posición sin pieza
                    result += "*";
                } else {
                    /// Contiene una pieza en esta posición. Por lo tanto, tiene un color asociado
                    switch (cell.getPiece().getColor()) {
                        case Orange:
                            result += "O";
                            break;
                        case Blue:
                            result += "B";
                            break;
                        case Green:
                            result += "G";
                            break;
                        case Red:
                            result += "R";
                            break;
                        case Yellow:
                            result += "Y";
                            break;
                        default:
                            result += "P";
                    }
                }
            }

            result += "\n";
        }
        return result;
    }

    /**
     * Función que comprueba si la posición dada esta dentro del tablero del juego
     *
     * @param row fila
     * @param col columna
     * @return true si (row, col) está dentro del tablero del juego
     */
    public boolean insideBoard(int row, int col) {
        /// Si esta fuera del tamaño físico de la matriz, no está dentro del tablero
        if (!((0 <= row && row < NUM_ROW) && (0 <= col && col < NUM_COL)))
            return false;

        return cells[row][col].insideBoard;
    }

    public Cell getCell(int row, int col) {
        if (!insideBoard(row, col)) {
            return null;
        }

        return this.cells[row][col];
    }
}
