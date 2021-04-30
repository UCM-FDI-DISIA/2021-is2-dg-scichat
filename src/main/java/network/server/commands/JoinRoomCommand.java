package network.server.commands;

import network.server.Room;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class JoinRoomCommand extends Command {

    public JoinRoomCommand() {
        super("JOIN_ROOM");
    }

    @Override
    public void execute(JSONObject body, Server server, WebSocket connection)
        throws Exception {
        JSONObject data = body.getJSONObject("data");
        String roomID = data.getString("roomID");
        String clientID = data.getString("clientID");

        if (!server.getRooms().containsKey(roomID)) {
            throw new Exception("Room ID " + roomID + " does not exist.");
        }

        Room room = server.getRooms().get(roomID);
        if (room.isFull()) {
            throw new Exception("Room ID " + roomID + " is full.");
        }

        room.addPlayer(clientID, connection);
    }
}
