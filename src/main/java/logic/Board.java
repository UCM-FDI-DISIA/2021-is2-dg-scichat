package logic;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import utils.PieceColor;

public class Board implements Serializable, Iterable<Cell> {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final int NUM_COL = 13;
    public static final int NUM_ROW = 17;

    private static final String[] SCHEMATIC = {
        "......U......",
        "......UU.....",
        ".....UUU.....",
        ".....UUUU....",
        "LLLL@@@@@RRRR",
        ".LLL@@@@@@RRR",
        ".LL@@@@@@@RR.",
        "..L@@@@@@@@R.",
        "..@@@@@@@@@..",
        "..l@@@@@@@@r.",
        ".ll@@@@@@@rr.",
        ".lll@@@@@@rrr",
        "llll@@@@@rrrr",
        ".....DDDD....",
        ".....DDD.....",
        "......DD.....",
        "......D......"
    };

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

                /// Crear una celda en el caso de que estÃ© dentro del tablero solamente
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

    public enum Side implements Serializable {
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

        /* MÃ©todos */

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
         * @param newCell La celda que queremos aÃ±adir al lado
         */
        private void addSideCells(Cell newCell) {
            sideCells.add(newCell);
        }

        public void clear() {
            sideCells.clear();
        }
    }

    public String toString() {
        String result =
            "    0   1   2   3   4   5   6   7   8   9  10  11  12\n" +
            "    |   |   |   |   |   |   |   |   |   |   |   |   |\n\n";
        for (int i = 0; i < NUM_ROW; ++i) {
            for (int isLow = 0; isLow < 2; isLow++) {
                String current = "";
                for (int j = 0; j < NUM_COL; ++j) {
                    Cell cell = cells[i][j];
                    if (cell == null) {
                        /// No es una posiciÃ³n del tablero
                        current += "    ";
                    } else if (cell.isEmpty()) {
                        /// PosiciÃ³n sin pieza
                        current += (isLow == 0 ? "┌─┐ " : "└─┘ ");
                    } else {
                        /// Contiene una pieza en esta posiciÃ³n. Por lo tanto, tiene un color asociado
                        PieceColor color = cell.getPiece().getColor();
                        current += color.getBoardString();
                    }
                }
                result +=
                    (
                        isLow == 1
                            ? (i < 10 ? " " + i : Integer.toString(i)) + "  "
                            : "  _ "
                    ) +
                    current.substring((i % 2) * 2) +
                    '\n';
            }
        }
        return result + "\n";
    }

    /**
     * FunciÃ³n que comprueba si la posiciÃ³n dada esta dentro del tablero del juego
     *
     * @param row fila
     * @param col columna
     * @return true si (row, col) estÃ¡ dentro del tablero del juego
     */
    public boolean insideBoard(int row, int col) {
        /// Si esta fuera del tamaÃ±o fÃ­sico de la matriz, no estÃ¡ dentro del tablero
        if (!((0 <= row && row < NUM_ROW) && (0 <= col && col < NUM_COL))) return false;

        return cells[row][col] != null;
    }

    /**
     * Get cell at a specific row and column
     *
     * @param row
     * @param col
     * @return
     */
    public Cell getCell(int row, int col) {
        if (!insideBoard(row, col)) {
            return null;
        }

        return this.cells[row][col];
    }

    @Override
    public Iterator<Cell> iterator() {
        return new Iterator<Cell>() {
            int row = 0, col = 6;
            boolean hasNext = true;

            @Override
            public boolean hasNext() {
                return hasNext;
            }

            @Override
            public Cell next() {
                hasNext = false;
                Cell rv = Board.this.getCell(row, col);
                int y = row, x = col;
                for (; y < NUM_ROW && !hasNext; ++y) {
                    for (x = (y == row ? col + 1 : 0); x < NUM_COL && !hasNext; ++x) {
                        hasNext = Board.this.insideBoard(y, x);
                        if (hasNext) x--; // Si hasNext, no queremos avanzar
                    }
                    if (hasNext) y--; // Si hasNext, no queremos avanzar
                }
                row = y;
                col = x;
                return rv;
            }
        };
    }
}
