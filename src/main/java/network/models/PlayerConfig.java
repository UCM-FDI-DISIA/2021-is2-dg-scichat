package network.models;

import logic.Board;
import org.json.JSONObject;
import utils.PieceColor;

public class PlayerConfig {
    public final PieceColor color;
    public final Board.Side side;
    private String name;

    public PlayerConfig(PieceColor _color, Board.Side _side, String _name) {
        this.color = _color;
        this.side = _side;
        this.name = _name;
    }

    public PlayerConfig(PieceColor _color, Board.Side _side) {
        this(_color, _side, null);
    }

    public PlayerConfig(JSONObject data) {
        int colorIndex = data.optInt("color", 0);
        int sideIndex = data.optInt("side", 0);

        this.color = PieceColor.values()[colorIndex];
        this.side = Board.Side.values()[sideIndex];
        if (data.has("name")) this.name = data.getString("name");
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public JSONObject toJSONObject() {
        JSONObject data = new JSONObject();

        data.put("color", this.color.ordinal());
        data.put("side", this.side.ordinal());
        data.put("name", this.name);

        return data;
    }
}
