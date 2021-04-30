package network.server.commands;

import network.server.Room;
import network.server.RoomConfig;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class CreateRoomCommand extends Command {

    public CreateRoomCommand() {
        super("CREATE_ROOM");
    }

    @Override
    public void execute(JSONObject body, Server server, WebSocket connection) {
        RoomConfig roomConfig = new RoomConfig(body.getJSONObject("data"));
        String roomID = Room.generateRoomID();
        Room room = new Room(roomConfig);

        server.getRooms().put(roomID, room);

        JSONObject data = roomConfig.toJSONObject();
        data.put("roomID", roomID);

        connection.send(
            new JSONObject().put("type", "ROOM_CREATED").put("data", data).toString()
        );

        System.out.println("Se ha creado una nueva habitación: ");
        System.out.println(data);
    }
}
