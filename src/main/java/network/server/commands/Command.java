package network.server.commands;

import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public abstract class Command {
    public final String type;

    public Command(String _type) {
        this.type = _type;
    }

    public abstract void execute(JSONObject body, Server server, WebSocket connection)
        throws Exception;

    public Command parse(String _type) {
        if (this.type.equals(_type)) return this;
        return null;
    }
}
