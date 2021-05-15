package utils;

import java.awt.*;
import org.json.JSONArray;

public class PieceColor extends Color {
    private String name;
    private String boardString;

    public static final PieceColor GREEN = new PieceColor(
        Color.GREEN.getRGB(),
        "GGG ",
        "Verde"
    );
    public static final PieceColor YELLOW = new PieceColor(
        Color.YELLOW.getRGB(),
        "YYY ",
        "Amarillo"
    );
    public static final PieceColor ORANGE = new PieceColor(
        Color.ORANGE.getRGB(),
        "OOO ",
        "Naranja"
    );
    public static final PieceColor RED = new PieceColor(
        Color.RED.getRGB(),
        "RRR ",
        "Rojo"
    );
    public static final PieceColor MAGENTA = new PieceColor(
        Color.MAGENTA.getRGB(),
        "PPP ",
        "Magenta"
    );

    public static final PieceColor[] availableColors = {
        GREEN,
        YELLOW,
        ORANGE,
        RED,
        MAGENTA
    };

    public static final PieceColor BLUE = new PieceColor(Color.BLUE.getRGB(), "BBB ");

    public PieceColor(int rgb, String _boardString, String _name) {
        super(rgb);
        this.name = _name;
        this.boardString = _boardString;
    }

    public PieceColor(int rgb, String _boardString) {
        this(rgb, _boardString, null);
    }

    public PieceColor(int rgb) {
        super(rgb);
        /// Intentar ver si hay un color con este RGB de los disponibles
        for (PieceColor c : PieceColor.availableColors) {
            if (c.getRGB() == rgb) {
                this.boardString = c.getBoardString();
            }
        }
    }

    public PieceColor(int r, int g, int b) {
        super(new Color(r, g, b).getRGB());
    }

    @Override
    public String toString() {
        if (this.name != null) return this.name;
        return (new ColorUtils()).getColorNameFromColor(this);
    }

    public JSONArray toJSONArray() {
        JSONArray result = new JSONArray();
        result.put(this.getRed());
        result.put(this.getGreen());
        result.put(this.getBlue());

        return result;
    }

    public String getBoardString() {
        return boardString;
    }
}
