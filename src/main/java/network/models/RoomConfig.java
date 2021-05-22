package network.models;

import java.util.LinkedList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Mode;

public class RoomConfig implements SocketMessage {
    protected Mode mode;
    protected int numPlayers;
    protected List<PlayerConfig> playerConfigs;

    public RoomConfig(Mode _mode, List<PlayerConfig> _playerConfigs) {
        this.mode = _mode;
        this.playerConfigs = _playerConfigs;
        this.numPlayers = _playerConfigs.size();
    }

    public RoomConfig(JSONObject data) {
        int modeIndex = data.optInt("mode", 0);
        this.mode = Mode.values()[modeIndex];
        this.numPlayers = data.getInt("numPlayers");

        JSONArray playerConfigArray = data.getJSONArray("playerConfigs");
        this.playerConfigs = new LinkedList<>();

        for (Object p : playerConfigArray) {
            playerConfigs.add(new PlayerConfig((JSONObject) p));
        }
    }

    public List<PlayerConfig> getPlayerConfigs() {
        return playerConfigs;
    }

    public void setMode(Mode mode) {
        this.mode = mode;
    }

    public void setNumPlayers(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    public void setPlayerConfigs(LinkedList<PlayerConfig> playerConfigs) {
        this.playerConfigs = playerConfigs;
    }

    public Mode getMode() {
        return mode;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("mode", this.mode.ordinal());
        data.put("numPlayers", this.numPlayers);
        data.put("playerConfigs", new JSONArray());

        for (PlayerConfig p : this.playerConfigs) {
            data.append("playerConfigs", p.toJSON());
        }

        return data;
    }
}
