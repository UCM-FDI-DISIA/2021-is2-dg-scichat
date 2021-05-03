package network.commands;

import network.client.SocketClient;
import network.models.Room;
import network.models.ServerRoom;
import network.models.SocketMessage;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class JoinRoomCommand extends Command {

    public JoinRoomCommand() {
        super("JOIN_ROOM");
    }

    @Override
    public SocketMessage execute(JSONObject _data, SocketClient connection) {
        /// Cuando el cliente recibe este commando, tiene que parsear a un Room
        Room room = new Room(_data);

        return room;
    }

    @Override
    public void execute(JSONObject data, Server server, WebSocket connection)
            throws Exception {
        String roomID = data.getString("roomID");
        String clientID = data.getString("clientID");

        if (!server.getRooms().containsKey(roomID)) {
            throw new Exception("Room ID " + roomID + " does not exist.");
        }

        ServerRoom serverRoom = server.getRooms().get(roomID);
        if (serverRoom.isFull()) {
            throw new Exception("Room ID " + roomID + " is full.");
        }

        serverRoom.addPlayer(clientID, connection);
    }
}
