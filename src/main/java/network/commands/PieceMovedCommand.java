package network.commands;

import org.json.JSONArray;
import org.json.JSONObject;

public class PieceMovedCommand extends Command {
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    private String roomID;
    public String playerID;

    public PieceMovedCommand() {
        super("PIECE_MOVED");
    }

    public PieceMovedCommand(
        int x1,
        int y1,
        int x2,
        int y2,
        String roomID,
        String playerID
    ) {
        this();
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.roomID = roomID;
        this.playerID = playerID;
    }

    @Override
    public void parseRequest(JSONObject req) {
        JSONObject data = req.getJSONObject("data");

        JSONArray fromArray = data.getJSONArray("from");
        JSONArray toArray = data.getJSONArray("to");

        this.x1 = fromArray.getInt(0);
        this.y1 = fromArray.getInt(1);
        this.x2 = toArray.getInt(0);
        this.y2 = toArray.getInt(1);

        this.playerID = data.getString("playerID");
    }

    @Override
    public JSONObject getData() {
        JSONObject data = new JSONObject();

        data.append("from", x1);
        data.append("from", y1);

        data.append("to", x2);
        data.append("to", y2);

        data.put("playerID", this.playerID);
        data.put("roomID", this.roomID);

        return data;
    }
}
