package network.models;

import java.util.HashMap;
import java.util.Map;
import org.java_websocket.WebSocket;
import org.json.JSONObject;

public class ServerRoom extends network.models.Room {
    private Map<String, WebSocket> playerConnections = new HashMap<>();

    public ServerRoom(RoomConfig config) {
        super(config);
    }

    public PlayerConfig addPlayer(String uuid, WebSocket connection) {
        PlayerConfig playerConfig = roomConfig.getPlayerConfigs().get(0);
        roomConfig.getPlayerConfigs().remove(0);
        this.players.put(uuid, playerConfig);
        this.playerConnections.put(uuid, connection);
        ++connectedPlayers;

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
        --connectedPlayers;

        broadCastRoomInfo();
    }

    public void broadCastRoomInfo() {
        JSONObject res = new JSONObject();
        res.put("type", "ROOM_INFO");

        JSONObject resData = this.toJSON();
        res.put("data", resData);

        for (Map.Entry<String, WebSocket> entry : this.playerConnections.entrySet()) {
            entry.getValue().send(res.toString());
        }
    }
}
