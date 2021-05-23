package network.models;

import logic.Board;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.PieceColor;

public class PlayerConfig implements SocketMessage {
    public final PieceColor color;
    public final Board.Side side;
    private String name;

    /**
     * Clase usada para transportar la información de configuración de un jugador
     *
     * @param _color color del jugador
     * @param _side  lado del tablero
     * @param _name  nombre
     */
    public PlayerConfig(PieceColor _color, Board.Side _side, String _name) {
        this.color = _color;
        this.side = _side;
        this.name = _name;
    }

    public PlayerConfig(PieceColor _color, Board.Side _side) {
        this(_color, _side, null);
    }

    /**
     * Este constructor se usa para parsear la información que se recibe de cliente / servidor
     *
     * @param data dato serializado en JSON
     */
    public PlayerConfig(JSONObject data) {
        JSONArray colorArray = data.getJSONArray("color");
        int R = colorArray.getInt(0);
        int G = colorArray.getInt(1);
        int B = colorArray.getInt(2);

        int sideIndex = data.optInt("side", 0);

        this.color = new PieceColor(R, G, B);
        this.side = Board.Side.values()[sideIndex];
        this.name = data.optString("name", null);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Serializar la clase en JSON
     * <p>
     * Es importante que, el formato que se usa aquí pueda ser parseado correctamente con el constructor anterior
     *
     * @return clase serializada en JSON
     */
    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();

        data.put("color", this.color.toJSONArray());
        data.put("side", this.side.ordinal());
        data.put("name", this.name);

        return data;
    }
}
