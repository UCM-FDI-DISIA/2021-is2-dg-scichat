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
        return this.ordinal();
    }

    public static PieceColor getPieceColor(int _index) {
        return PieceColor.values()[_index];
    }

    public String getBoardString() {
        return boardString;
    }

    public String toString() {
        return name;
    }
}
