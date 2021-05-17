package network.commands;

import network.client.SocketClient;
import org.json.JSONObject;

public class RematchCommand extends Command {
    private String roomID;

    public RematchCommand() {
        super("REMATCH");
    }

    public RematchCommand(String _roomID) {
        this();
        this.roomID = _roomID;
    }

    @Override
    public void send(SocketClient connection) {
        JSONObject req = new JSONObject();
        req.put("type", this.type);

        JSONObject data = new JSONObject();
        data.put("roomID", this.roomID);

        req.put("data", data);

        connection.send(req.toString());
    }
}
