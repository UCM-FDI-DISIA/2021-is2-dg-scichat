package network.commands;

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

    /**
     * Este constructor se usa para cuando hay que mandar petición al servidor
     * Le pasamos los datos que necesitamos mandar
     *
     * @param _config configuración de la sala que queremos crear
     */
    public CreateRoomCommand(RoomConfig _config) {
        this();
        this.config = _config;
    }

    @Override
    public JSONObject getData() {
        return config.toJSON();
    }

    @Override
    public void execute(JSONObject req, Server server, WebSocket connection) {
        JSONObject _data = req.getJSONObject("data");

        /// Parsear la configuración de la habitación
        RoomConfig roomConfig = new RoomConfig(_data);

        /// Generar un ID aleatorio para la habitación
        String roomID = ServerRoom.generateRoomID();

        /// Crear la sala con la configuración dada
        ServerRoom serverRoom = new ServerRoom(roomConfig);
        server.getRooms().put(roomID, serverRoom);

        /// Avisar al cliente de que se ha creado la nueva habitación
        JSONObject data = roomConfig.toJSON();
        data.put("roomID", roomID);

        connection.send(
            new JSONObject().put("type", "ROOM_CREATED").put("data", data).toString()
        );

        System.out.println("Se ha creado una nueva habitación: ");
        System.out.println(_data);
    }
}
