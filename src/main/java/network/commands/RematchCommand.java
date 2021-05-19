package network.commands;

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
    public JSONObject getData() {
        JSONObject data = new JSONObject();
        data.put("roomID", this.roomID);

        return data;
    }
}
