package network.commands;

import org.json.JSONObject;

public class SurrenderCommand extends Command {
    private String roomID;
    private String playerID;

    public SurrenderCommand() {
        super("SURRENDER");
    }

    /**
     * @param _playerID ID del jugador que se ha rendido
     * @param _roomID   ID de la habitación
     */
    public SurrenderCommand(String _playerID, String _roomID) {
        this();
        this.playerID = _playerID;
        this.roomID = _roomID;
    }

    @Override
    public JSONObject getData() {
        JSONObject data = new JSONObject();

        data.put("roomID", this.roomID);
        data.put("playerID", this.playerID);

        return data;
    }

    @Override
    public void parseRequest(JSONObject req) {
        JSONObject data = req.getJSONObject("data");
        this.playerID = data.getString("playerID");
    }

    public String getPlayerID() {
        return playerID;
    }
}
