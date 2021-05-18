package network.commands;

import network.client.SocketClient;
import network.models.ServerRoom;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class SurrenderCommand extends Command {
    private String roomID;
    private String playerID;

    public SurrenderCommand() {
        super("SURRENDER");
    }

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
    public void parseRequest(JSONObject data) {
        this.playerID = data.getString("playerID");
    }

    public String getPlayerID() {
        return playerID;
    }
}
