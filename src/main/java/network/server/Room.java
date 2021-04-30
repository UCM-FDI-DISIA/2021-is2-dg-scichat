package network.server;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import network.server.PlayerConfig;
import network.server.RoomConfig;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class Room {
    private RoomConfig roomConfig;
    private Map<String, PlayerConfig> players = new TreeMap<>();
    private Map<String, WebSocket> playerConnections = new HashMap<>();

    public Room(RoomConfig config) {
        this.roomConfig = config;
    }

    public boolean isFull() {
        /// Esta lleno cuando hay tantos jugadores conectados como configurados
        return this.getConnectedPlayers() == this.roomConfig.getNumPlayers();
    }

    public PlayerConfig addPlayer(String uuid, WebSocket connection) {
        PlayerConfig playerConfig = roomConfig.getPlayerConfigs().poll();
        this.players.put(uuid, playerConfig);
        this.playerConnections.put(uuid, connection);

        /// Cuando se produce un cambio en jugador, enviar este cambio a todos los clientes de la habitación
        broadCastRoomInfo();

        return playerConfig;
    }

    public void removePlayer(String uuid) {
        if (!this.players.containsKey(uuid)) return;

        PlayerConfig playerConfig = this.players.get(uuid);
        this.roomConfig.getPlayerConfigs().add(playerConfig);

        this.players.remove(uuid);
        this.playerConnections.remove(uuid);

        broadCastRoomInfo();
    }

    public int getConnectedPlayers() {
        return this.playerConnections.size();
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
        data.put("connectedPlayers", this.getConnectedPlayers());
        JSONObject players = new JSONObject();

        for (Map.Entry<String, PlayerConfig> entry : this.players.entrySet()) {
            players.put(entry.getKey(), entry.getValue().toJSONObject());
        }

        data.put("players", players);
        return data;
    }

    public void broadCastRoomInfo() {
        JSONObject res = new JSONObject();
        res.put("type", "ROOM_INFO");

        JSONObject resData = this.toJSONObject();
        res.put("data", resData);

        for (Map.Entry<String, WebSocket> entry : this.playerConnections.entrySet()) {
            entry.getValue().send(res.toString());
        }
    }
}
