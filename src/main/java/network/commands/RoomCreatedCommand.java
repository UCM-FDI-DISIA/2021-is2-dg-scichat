package network.commands;

import org.json.JSONObject;

public class RoomCreatedCommand extends Command {
    private String roomID;

    public RoomCreatedCommand() {
        super("ROOM_CREATED");
    }

    public RoomCreatedCommand(String _roomID) {
        this();
        this.roomID = _roomID;
    }

    @Override
    public JSONObject getData() {
        return new JSONObject().put("roomID", roomID);
    }

    public String getRoomID() {
        return roomID;
    }

    @Override
    public void parseRequest(JSONObject req) {
        roomID = req.getJSONObject("data").getString("roomID");
    }
}
