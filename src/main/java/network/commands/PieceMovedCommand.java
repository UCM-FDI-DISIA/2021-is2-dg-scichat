package network.commands;

import logic.Cell;
import network.client.SocketClient;
import network.models.ServerRoom;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class PieceMovedCommand extends Command {
    private Cell from;
    private Cell to;
    private String roomID;

    public PieceMovedCommand() {
        super("PIECE_MOVED");
    }

    public PieceMovedCommand(Cell from, Cell piece, String roomID) {
        this();
        this.from = from;
        this.to = piece;
        this.roomID = roomID;
    }

    @Override
    public void send(SocketClient connection) {
        JSONObject req = new JSONObject();
        req.put("type", this.type);

        JSONObject data = new JSONObject();

        data.append("from", this.from.getRow());
        data.append("from", this.from.getCol());

        data.append("to", this.to.getRow());
        data.append("to", this.to.getCol());

        data.put("clientID", connection.getClientID());
        data.put("roomID", this.roomID);

        req.put("data", data);

        connection.send(req.toString());
    }

    @Override
    public void execute(JSONObject data, Server server, WebSocket connection)
        throws Exception {
        String roomID = data.getString("roomID");
        ServerRoom room = server.getRooms().get(roomID);

        JSONObject req = new JSONObject();
        req.put("type", this.type);
        req.put("data", data);

        room.broadCast(req.toString());
    }
}
