package network.models;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

public class Room implements SocketMessage {
    protected RoomConfig roomConfig;
    protected Map<String, PlayerConfig> players = new TreeMap<>();

    protected int connectedPlayers;

    public Room(RoomConfig config) {
        this.roomConfig = config;
    }

    public Room(JSONObject data) {
        /// Crear el objeto a partir de JSONObject del servidor
        this.roomConfig = new RoomConfig(data);

        this.connectedPlayers = data.getInt("connectedPlayers");

        JSONArray playerIDs = data.getJSONObject("players").names();
        for (Object _ID : playerIDs) {
            String ID = (String) _ID;
            this.players.put(
                    ID,
                    new PlayerConfig(data.getJSONObject("players").getJSONObject(ID))
                );
        }
    }

    public RoomConfig getRoomConfig() {
        return roomConfig;
    }

    public Map<String, PlayerConfig> getPlayers() {
        return players;
    }

    public PlayerConfig getPlayer(String playerID) throws Exception {
        if (!this.players.containsKey(playerID)) {
            throw new Exception("Player ID " + playerID + " does not exist.");
        }

        return this.players.get(playerID);
    }

    public int getConnectedPlayers() {
        return connectedPlayers;
    }

    public boolean isFull() {
        /// Esta lleno cuando hay tantos jugadores conectados como configurados
        return this.getConnectedPlayers() == this.roomConfig.getNumPlayers();
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

    public JSONObject toJSON() {
        JSONObject data = this.roomConfig.toJSON();
        data.put("connectedPlayers", this.getConnectedPlayers());
        JSONObject players = new JSONObject();

        for (Map.Entry<String, PlayerConfig> entry : this.players.entrySet()) {
            players.put(entry.getKey(), entry.getValue().toJSONObject());
        }

        data.put("players", players);
        return data;
    }
}
