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

    @Override
    public JSONObject getData() {
        return new JSONObject().put("roomID", this.roomID);
    }
}
