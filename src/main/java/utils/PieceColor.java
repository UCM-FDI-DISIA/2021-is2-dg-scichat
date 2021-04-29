package utils;

import java.awt.*;

public enum PieceColor {
    GREEN(Color.GREEN, "Verde", "GGG "),
    YELLOW(Color.YELLOW, "Amarillo", "YYY "),
    ORANGE(Color.ORANGE, "Naranja", "OOO "),
    RED(Color.RED, "Rojo", "RRR "),
    MAGENTA(Color.MAGENTA, "Magenta", "PPP "),
    BLUE(Color.BLUE, "Azul", "BBB ");

    private final Color color;
    private final String name;
    private final String boardString;

    PieceColor(Color _color, String _name, String _boardString) {
        this.color = _color;
        this.name = _name;
        this.boardString = _boardString;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public int getJSONValue() {
        switch (this.name) {
            case ("Verde"):
                return 0;
            case ("Amarillo"):
                return 1;
            case ("Naranja"):
                return 2;
            case ("Rojo"):
                return 3;
            case ("Magenta"):
                return 4;
            case ("Blue"):
                return 5;
        }
        return -1;
    }

    public static PieceColor getPieceColor(int color) {
        switch (color) {
            case (0):
                return PieceColor.GREEN;
            case (1):
                return PieceColor.YELLOW;
            case (2):
                return PieceColor.ORANGE;
            case (3):
                return PieceColor.RED;
            case (4):
                return PieceColor.MAGENTA;
            case (5):
                return PieceColor.BLUE;
        }
        return null;
    }

    public String getBoardString() {
        return boardString;
    }

    public String toString() {
        return name;
    }
}
