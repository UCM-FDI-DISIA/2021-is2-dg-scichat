package network.commands;

import network.models.Room;
import network.models.ServerRoom;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class JoinRoomCommand extends Command {
    private String roomID;
    private Room room;
    private String name;

    public JoinRoomCommand() {
        super("JOIN_ROOM");
    }

    /**
     * @param roomID ID de la habitación que quiere entrar
     * @param name   Nombre del jugador
     */
    public JoinRoomCommand(String roomID, String name) {
        this();
        this.roomID = roomID;
        this.name = name;
    }

    @Override
    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("roomID", this.roomID);
        data.put("name", this.name);

        return data;
    }

    @Override
    public void parseRequest(JSONObject req) {
        /// Cuando el cliente recibe este commando, tiene que parsear a un Room
        this.room = new Room(req.getJSONObject("data"));
    }

    @Override
    public void execute(JSONObject req, Server server, WebSocket connection)
        throws Exception {
        JSONObject data = req.getJSONObject("data");
        String clientID = req.getString("clientID");

        String roomID = data.getString("roomID");
        String name = data.getString("name");

        ServerRoom serverRoom = server.getRoom(roomID);

        /// Añadir el jugador a la habitación
        /// También se enviará la actualización al resto de jugadores conectados
        serverRoom.addPlayer(clientID, name, connection);
    }
}
