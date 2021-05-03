package network.commands;

import network.client.SocketClient;
import network.models.Room;
import network.models.SocketMessage;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class RoomInfoCommand extends Command {

    public RoomInfoCommand() {
        super("ROOM_INFO");
    }

    @Override
    public SocketMessage execute(JSONObject data, SocketClient connection) {
        /// Si recibe una información de la habitación, actualizar el GUI
        return new Room(data);
    }

    @Override
    public void execute(JSONObject data, Server server, WebSocket connection)
        throws Exception {
        /// El servidor de momento no recibe esta petition
    }
}
