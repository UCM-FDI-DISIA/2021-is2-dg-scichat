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
    public void send(SocketClient connection) {
        JSONObject req = new JSONObject();
        req.put("type", this.type);
        req.put("clientID", connection.getClientID());
        req.put("data", this.getData());

        connection.send(req.toString());
    }

    public abstract JSONObject getData();

    /// Para cuando el cliente recibe el mensaje, parsear el componente
    public void parseRequest(JSONObject data) {}

    /// Para ejecutar en el cliente
    public void execute(JSONObject data, SocketClient connection) {
        this.parseRequest(data);
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

        ServerRoom room = server.getRoom(roomID);

        JSONObject req = new JSONObject();
        req.put("type", this.type);
        req.put("data", data);

        room.broadCast(req.toString());
    }

    public final Command parseCommand(String _type) {
        if (this.type.equals(_type)) return this;
        return null;
    }
}
