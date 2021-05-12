package network.commands;

import network.models.Room;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class RoomInfoCommand extends Command {
    private Room room;

    public RoomInfoCommand() {
        super("ROOM_INFO");
    }

    @Override
    public void parseRequest(JSONObject data) {
        /// Si recibe una información de la habitación, actualizar el GUI
        this.room = new Room(data);
    }

    public Room getRoom() {
        return room;
    }

    @Override
    public void execute(JSONObject data, Server server, WebSocket connection)
        throws Exception {
        /// El servidor de momento no recibe esta petition
    }
}
