package network.commands;

import network.client.SocketClient;
import network.models.ServerRoom;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public abstract class Command {
    public final String type;

    public Command(String _type) {
        this.type = _type;
    }

    /// Para enviar este commando al servidor
    public void send(SocketClient connection) {}

    /// Para cuando el cliente recibe el mensaje, parsear el componente
    public void parse(JSONObject data) {}

    /// Para ejecutar en el cliente
    public void execute(JSONObject data, SocketClient connection) {
        this.parse(data);
    }

    /// Ejecutar para servidor
    public void execute(JSONObject data, Server server, WebSocket connection)
        throws Exception {
        /// Por defecto, reenvía la información a todos los jugadores de la habitación
        this.broadCast(data, server, connection);
    }

    public final void broadCast(JSONObject data, Server server, WebSocket connection)
        throws Exception {
        if (!data.has("roomID")) return;
        String roomID = data.getString("roomID");

        if (!server.getRooms().containsKey(roomID)) {
            throw new Exception("Room ID " + roomID + " does not exist.");
        }

        ServerRoom room = server.getRooms().get(roomID);

        JSONObject req = new JSONObject();
        req.put("type", this.type);
        req.put("data", data);

        room.broadCast(req.toString());
    }

    public Command parse(String _type) {
        if (this.type.equals(_type)) return this;
        return null;
    }
}
