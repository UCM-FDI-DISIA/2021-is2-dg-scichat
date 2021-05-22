package network.commands;

import network.models.Room;
import org.json.JSONObject;

public class RoomInfoCommand extends Command {
    private Room room;

    public RoomInfoCommand() {
        super("ROOM_INFO");
    }

    @Override
    public JSONObject getData() {
        /// No se manda de momento esta petición al servidor
        return new JSONObject();
    }

    @Override
    public void parseRequest(JSONObject req) {
        /// Si recibe una información de la habitación, actualizar el GUI
        this.room = new Room(req.getJSONObject("data"));
    }

    public Room getRoom() {
        return room;
    }
}
