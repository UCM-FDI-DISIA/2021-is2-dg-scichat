package network.server;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import network.server.PlayerConfig;
import network.server.RoomConfig;
import org.json.JSONObject;

public class Room {
    private RoomConfig roomConfig;
    private Map<String, PlayerConfig> players = new TreeMap<>();
    private int connectedPlayers = 0;

    public Room(RoomConfig config) {
        this.roomConfig = config;
    }

    public boolean isFull() {
        /// Esta lleno cuando hay tantos jugadores conectados como configurados
        return this.connectedPlayers == this.roomConfig.getNumPlayers();
    }

    public PlayerConfig addPlayer(String uuid) {
        PlayerConfig playerConfig = roomConfig.getPlayerConfigs().poll();
        ++this.connectedPlayers;

        return playerConfig;
    }

    public void removePlayer(String uuid) {
        PlayerConfig playerConfig = this.players.get(uuid);
        this.roomConfig.getPlayerConfigs().add(playerConfig);
        --this.connectedPlayers;
    }

    public int getConnectedPlayers() {
        return connectedPlayers;
    }

    public static String generateRoomID() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    public JSONObject toJSONObject() {
        JSONObject data = this.roomConfig.toJSONObject();
        data.put("connectedPlayers", this.connectedPlayers);
        JSONObject players = new JSONObject();

        for (Map.Entry<String, PlayerConfig> entry : this.players.entrySet()) {
            players.put(entry.getKey(), entry.getValue().toJSONObject());
        }

        data.put("players", players);
        return data;
    }
}
