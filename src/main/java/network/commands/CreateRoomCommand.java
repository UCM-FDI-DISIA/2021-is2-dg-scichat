package network.commands;

import network.client.SocketClient;
import network.models.RoomConfig;
import network.models.ServerRoom;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class CreateRoomCommand extends Command {
    RoomConfig config;

    public CreateRoomCommand() {
        super("CREATE_ROOM");
    }

    public CreateRoomCommand(RoomConfig _config) {
        this();
        this.config = _config;
    }

    @Override
    public JSONObject getData() {
        return config.toJSON();
    }

    @Override
    public void execute(JSONObject _data, Server server, WebSocket connection) {
        RoomConfig roomConfig = new RoomConfig(_data);
        String roomID = ServerRoom.generateRoomID();

        ServerRoom serverRoom = new ServerRoom(roomConfig);
        server.getRooms().put(roomID, serverRoom);

        JSONObject data = roomConfig.toJSON();
        data.put("roomID", roomID);

        connection.send(
            new JSONObject().put("type", "ROOM_CREATED").put("data", data).toString()
        );

        System.out.println("Se ha creado una nueva habitación: ");
        System.out.println(_data);
    }
}
