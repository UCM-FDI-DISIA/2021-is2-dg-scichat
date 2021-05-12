package network.commands;

import network.client.SocketClient;
import network.models.Room;
import network.models.ServerRoom;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class JoinRoomCommand extends Command {
    private String roomID;
    private String clientID;
    private Room room;
    private String name;

    public JoinRoomCommand() {
        super("JOIN_ROOM");
    }

    public JoinRoomCommand(String roomID, String clientID, String name) {
        this();
        this.roomID = roomID;
        this.clientID = clientID;
        this.name = name;
    }

    @Override
    public void send(SocketClient connection) {
        /// Mandar una petición al servidor para entrar en habitación
        JSONObject req = new JSONObject();
        req.put("type", this.type);

        JSONObject data = new JSONObject();
        data.put("clientID", this.clientID);
        data.put("roomID", this.roomID);
        data.put("name", this.name);

        req.put("data", data);
        connection.send(req.toString());
    }

    @Override
    public void parseRequest(JSONObject data) {
        /// Cuando el cliente recibe este commando, tiene que parsear a un Room
        this.room = new Room(data);
    }

    @Override
    public void execute(JSONObject data, Server server, WebSocket connection)
        throws Exception {
        String roomID = data.getString("roomID");
        String clientID = data.getString("clientID");
        String name = data.getString("name");

        ServerRoom serverRoom = server.getRoom(roomID);

        if (serverRoom.isFull()) {
            throw new Exception("Room ID " + roomID + " is full.");
        }

        serverRoom.addPlayer(clientID, name, connection);
    }
}
