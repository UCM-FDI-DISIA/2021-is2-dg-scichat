package utils;

import java.awt.*;

public enum PieceColor {
    GREEN(Color.GREEN, "Verde"),
    YELLOW(Color.YELLOW, "Amarillo"),
    ORANGE(Color.ORANGE, "Naranja"),
    RED(Color.RED, "Rojo"),
    MAGENTA(Color.MAGENTA, "Magenta"),
    BLUE(Color.BLUE, "Azul");

    private final Color color;
    private final String name;

    PieceColor(Color _color, String _name) {
        this.color = _color;
        this.name = _name;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }
}