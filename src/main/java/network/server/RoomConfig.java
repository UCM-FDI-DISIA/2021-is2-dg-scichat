package network.server;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import network.server.PlayerConfig;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Mode;

public class RoomConfig {
    private Mode mode;
    private LinkedList<PlayerConfig> playerConfigs;
    private int numPlayers;

    public RoomConfig(Mode _mode, List<PlayerConfig> _playerConfigs) {
        this.mode = _mode;
        this.playerConfigs = new LinkedList<>(_playerConfigs);
        this.numPlayers = _playerConfigs.size();
    }

    public RoomConfig(JSONObject data) {
        int modeIndex = data.optInt("mode", 0);
        this.mode = Mode.values()[modeIndex];

        JSONArray playerConfigArray = data.getJSONArray("players");
        this.playerConfigs = new LinkedList<>();

        for (Object p : playerConfigArray) {
            playerConfigs.add(new PlayerConfig((JSONObject) p));
        }

        this.numPlayers = this.playerConfigs.size();
    }

    public Queue<PlayerConfig> getPlayerConfigs() {
        return playerConfigs;
    }

    public Mode getMode() {
        return mode;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public JSONObject toJSONObject() {
        JSONObject data = new JSONObject();
        data.put("mode", this.mode.ordinal());

        for (PlayerConfig p : this.playerConfigs) {
            data.append("players", p.toJSONObject());
        }

        return data;
    }
}
