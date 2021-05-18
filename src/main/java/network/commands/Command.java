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

    public JSONObject getData() {
        return new JSONObject();
    }

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
        this.broadCast(data, server, connection, true);
    }

    public final void broadCast(
        JSONObject req,
        Server server,
        WebSocket connection,
        boolean toSender
    )
        throws Exception {
        JSONObject data = req.getJSONObject("data");
        String clientID = req.getString("clientID");

        if (!data.has("roomID")) return;
        String roomID = data.getString("roomID");

        ServerRoom room = server.getRoom(roomID);

        if (toSender) {
            room.broadCast(req.toString(), null);
        } else {
            room.broadCast(req.toString(), clientID);
        }
    }

    public final Command parseCommand(String _type) {
        if (this.type.equals(_type)) return this;
        return null;
    }
}
