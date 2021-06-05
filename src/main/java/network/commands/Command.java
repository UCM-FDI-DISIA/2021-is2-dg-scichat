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

    /**
     * Método creado para mandar una petición al servidor, con los datos que lleva la instancia
     *
     * @param connection conexión de Socket
     */
    public void send(SocketClient connection) {
        JSONObject req = new JSONObject();
        req.put("type", this.type);
        req.put("clientID", connection.getClientID());

        /// El método getData lo tiene que sobreescribir cada comando, con los datos que quiere enviar
        req.put("data", this.getData());

        connection.send(req.toString());
    }

    /**
     * Método creado para mandar una respuesta al cliente, con los datos que lleva la instancia
     *
     * @param connection conexión de Socket con el cliente
     */
    public void send(WebSocket connection) {
        JSONObject req = new JSONObject();
        req.put("type", this.type);

        /// El método getData lo tiene que sobreescribir cada comando, con los datos que quiere enviar
        req.put("data", this.getData());

        connection.send(req.toString());
    }

    /**
     * @return JSONObject que se enviará en campo `data` de la petición al servidor
     */
    public JSONObject getData() {
        return new JSONObject();
    }

    /**
     * Para parsear la petición
     *
     * @param req petición en formato JSON
     */
    public void parseRequest(JSONObject req) {}

    /**
     * Acción a realizar por el cliente cuando recibe el comando
     *
     * @param req        cuerpo de petición en JSON
     * @param connection conexión Socket
     */
    public void execute(JSONObject req, SocketClient connection) {
        this.parseRequest(req);
    }

    /**
     * Acción a realizar por el servidor cuando recibe el comando
     *
     * @param req        cuerpo de petición
     * @param server     instancia del servidor
     * @param connection conexión de Socket con el emisor del mensaje
     */
    public void execute(JSONObject req, Server server, WebSocket connection)
        throws Exception {
        /// Por defecto, reenvía la información a todos los jugadores de la habitación
        this.broadCast(req, server, connection, true);
    }

    /**
     * Para reenviar la petición al resto de jugadores de la habitación
     * <p>
     * Para ello, la petición tiene que contener un atributo data.roomID
     *
     * @param req        cuerpo de petición
     * @param server     instancia del servidor
     * @param connection conexión del Socket con el emisor
     * @param toSender   si se reenvía el mismo mensaje al emisor
     */
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

    /**
     * Método para parsear el comando, usado por {@link CommandParser}
     *
     * @param _type string que representa el tipo del comando
     * @return la instancia del comando, si coincide el tipo
     */
    public final Command parseCommand(String _type) {
        if (this.type.equals(_type)) return this;
        return null;
    }
}
