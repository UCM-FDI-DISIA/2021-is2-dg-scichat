package network.commands;

import network.models.PlayerConfig;
import network.models.ServerRoom;
import network.server.Server;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class SetPlayerNameCommand extends Command {
    private String name;
    private String playerID;
    private String roomID;

    public SetPlayerNameCommand() {
        super("SET_PLAYER_NAME");
    }

    public SetPlayerNameCommand(String _name, String _playerID, String _roomID) {
        this();
        this.name = _name;
        this.playerID = _playerID;
        this.roomID = _roomID;
    }

    @Override
    public void parseRequest(JSONObject data) {
        this.name = data.getString("name");
        this.playerID = data.getString("playerID");
        this.roomID = data.getString("roomID");
    }

    public String getPlayerID() {
        return playerID;
    }

    public String getName() {
        return name;
    }

    @Override
    public void execute(JSONObject data, Server server, WebSocket connection)
        throws Exception {
        this.parseRequest(data);

        ServerRoom room = server.getRoom(roomID);
        PlayerConfig player = room.getPlayer(playerID);

        player.setName(name);

        room.broadCastRoomInfo();
    }
}
