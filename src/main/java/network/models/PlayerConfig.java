package network.models;

import logic.Board;
import org.json.JSONObject;
import utils.PieceColor;

public class PlayerConfig {
    public final PieceColor color;
    public final Board.Side side;
    private String name;

    public PlayerConfig(PieceColor _color, Board.Side _side) {
        this.color = _color;
        this.side = _side;
    }

    public PlayerConfig(JSONObject data) {
        int colorIndex = data.optInt("color", 0);
        int sideIndex = data.optInt("side", 0);

        this.color = PieceColor.values()[colorIndex];
        this.side = Board.Side.values()[sideIndex];
    }

    public JSONObject toJSONObject() {
        JSONObject data = new JSONObject();

        data.put("color", this.color.ordinal());
        data.put("side", this.side.ordinal());

        return data;
    }
}
