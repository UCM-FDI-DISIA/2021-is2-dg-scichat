package network.commands;

import network.client.SocketClient;
import network.models.ServerRoom;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class StartGameCommand extends Command {
    private String roomID;

    public StartGameCommand() {
        super("START_GAME");
    }

    public StartGameCommand(String _roomID) {
        this();
        this.roomID = _roomID;
    }

    @Override
    public void send(SocketClient connection) {
        JSONObject req = new JSONObject();
        req.put("type", this.type);

        /// Pasar como _data { roomID }
        req.put("data", new JSONObject().put("roomID", this.roomID));
        connection.send(req.toString());
    }
}
