package logic;

import java.awt.Color;
import java.util.HashSet;

public class Board {

    private static final int NUM_COL = 13;
    private static final int NUM_ROW = 17;

    private static final String[] SCHEMATIC = {"......U......", "......UU.....", ".....UUU.....", ".....UUUU....",
            "LLLL@@@@@RRRR", ".LLL@@@@@@RRR", ".LL@@@@@@@RR.", "..L@@@@@@@@R.", "..@@@@@@@@@..", "..l@@@@@@@@r.",
            ".ll@@@@@@@rr.", ".lll@@@@@@rrr", "llll@@@@@rrrr", ".....DDDD....", ".....DDD.....", "......DD.....",
            "......D......"};

    /// Matriz de celdas
    private final Cell[][] cells = new Cell[NUM_ROW][NUM_COL];

    public Board() {

        Side.Down.clear();
        Side.DownLeft.clear();
        Side.DownRight.clear();
        Side.UpLeft.clear();
        Side.UpRight.clear();
        Side.Up.clear();
        for (int i = 0; i < NUM_ROW; ++i) {
            for (int j = 0; j < NUM_COL; ++j) {
                /// Comprobar si esta dentro del tablero
                boolean insideBoard = (SCHEMATIC[i].charAt(j) != '.');

                /// Crear una celda en el caso de que esté dentro del tablero solamente
                Cell cell = (insideBoard ? new Cell(i, j, this) : null);
                cells[i][j] = cell;

                switch (SCHEMATIC[i].charAt(j)) {
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
        Down(0, new HashSet<Cell>()), // 0
        DownLeft(1, new HashSet<Cell>()), // 1
        UpLeft(2, new HashSet<Cell>()), // 2
        Up(3, new HashSet<Cell>()), // 3
        UpRight(4, new HashSet<Cell>()), // 4
        DownRight(5, new HashSet<Cell>()); // 5

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

        public void clear() {
            sideCells.clear();
        }
    }

	public String toString() {
		String result = "    0   1   2   3   4   5   6   7   8   9  10  11  12\n" +
				"    |   |   |   |   |   |   |   |   |   |   |   |   |\n\n";
		for (int i = 0; i < NUM_ROW; ++i) {
			for (int isLow = 0; isLow < 2; isLow++) {
				String current = "";
				for (int j = 0; j < NUM_COL; ++j) {
					Cell cell = cells[i][j];
					if (cell == null) {
						/// No es una posición del tablero
						current += "    ";
					} else if (cell.isEmpty()) {
						/// Posición sin pieza
						current += (isLow == 0 ? "┌─┐ " : "└─┘ ");
					} else {
						/// Contiene una pieza en esta posición. Por lo tanto, tiene un color asociado
						Color val = cell.getPiece().getColor();
						if(val == Color.GREEN) {
							current += "GGG ";
						}
						else if(val == Color.YELLOW) {
							current += "YYY ";
						}
						else if(val == Color.ORANGE) {
							current += "OOO ";
						}
						else if(val == Color.RED) {
							current += "RRR ";
						}
						else if(val == Color.MAGENTA) {
							current += "PPP ";
						}
						else if(val == Color.BLUE) {
							current += "BBB ";
						}
						else {
							current += "--- ";
						}
					}
				}
				result += (isLow == 1?(i < 10? " " + i : Integer.toString(i)) + "  " : "  _ ") + current.substring((i % 2)*2) + '\n';
			}
		}
		return result + "\n";
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

        return cells[row][col] != null;
    }

    public Cell getCell(int row, int col) {
        if (!insideBoard(row, col)) {
            return null;
        }

        return this.cells[row][col];
    }
}
