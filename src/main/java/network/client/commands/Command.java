package network.client.commands;

import network.client.SocketClient;
import org.json.JSONObject;

public abstract class Command {
    public final String type;

    public Command(String _type) {
        this.type = _type;
    }

    public abstract void execute(JSONObject body, SocketClient connection);

    public Command parse(String _type) {
        if (this.type.equals(_type)) return this;
        return null;
    }
}
