package network.models;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Clase que representa una sala del juego
 * Tiene una configuración de la sala, que lleva el modo del juego y la configuración de los jugadores
 * Y un Map que hace la correspondencia entre ID del jugador y su configuración asignada
 */
public class Room implements SocketMessage {
    protected final RoomConfig roomConfig;
    protected final Map<String, PlayerConfig> players = new TreeMap<>();

    protected int connectedPlayers;

    /**
     * Crear una nueva sala con una configuración de sala
     */
    public Room(RoomConfig config) {
        this.roomConfig = config;
    }

    /**
     * Parsear la información de sala a partir de la información enviada por el servidor
     */
    public Room(JSONObject data) {
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

    /**
     * Método para generar un ID aleatorio para la sala, de 6 caracteres
     */
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

    /**
     * Serializar la información de sala en JSON
     */
    public JSONObject toJSON() {
        JSONObject data = this.roomConfig.toJSON();
        data.put("connectedPlayers", this.getConnectedPlayers());
        JSONObject players = new JSONObject();

        for (Map.Entry<String, PlayerConfig> entry : this.players.entrySet()) {
            players.put(entry.getKey(), entry.getValue().toJSON());
        }

        data.put("players", players);
        return data;
    }
}
