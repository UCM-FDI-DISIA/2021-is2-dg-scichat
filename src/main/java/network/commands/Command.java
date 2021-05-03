package network.commands;

import network.client.SocketClient;
import network.models.SocketMessage;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public abstract class Command {
    public final String type;

    public Command(String _type) {
        this.type = _type;
    }

    /// Ejecutar para cliente
    public abstract SocketMessage execute(JSONObject _data, SocketClient connection);

    /// Ejecutar para servidor
    public abstract void execute(JSONObject data, Server server, WebSocket connection) throws Exception;

    public Command parse(String _type) {
        if (this.type.equals(_type)) return this;
        return null;
    }
}
