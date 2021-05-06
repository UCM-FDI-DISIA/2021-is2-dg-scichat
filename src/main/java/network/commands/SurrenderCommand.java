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
    public void send(SocketClient connection) {
        JSONObject req = new JSONObject();
        req.put("type", this.type);
        JSONObject data = new JSONObject();

        data.put("roomID", this.roomID);
        data.put("playerID", this.playerID);

        req.put("data", data);

        connection.send(req.toString());
    }

    @Override
    public void parse(JSONObject data) {
        this.playerID = data.getString("playerID");
    }

    public String getPlayerID() {
        return playerID;
    }
}
