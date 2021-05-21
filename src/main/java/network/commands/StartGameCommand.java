package network.commands;

import org.json.JSONObject;

public class StartGameCommand extends Command {
    private String roomID;

    public StartGameCommand() {
        super("START_GAME");
    }

    public StartGameCommand(String _roomID) {
        this();
        this.roomID = _roomID;
    }

    @Override
    public JSONObject getData() {
        return new JSONObject().put("roomID", this.roomID);
    }
}
