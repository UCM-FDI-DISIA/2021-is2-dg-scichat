package network.models;

import java.util.HashMap;
import java.util.Map;
import network.commands.RoomInfoCommand;
import network.commands.SurrenderCommand;
import org.java_websocket.WebSocket;

/**
 * Versión en servidor del modelo Room
 * Se añade un nuevo atributo playerConnections, para almacenar las conexiones Socket de los jugadores conectados a la sala
 */
public class ServerRoom extends network.models.Room {
    private Map<String, WebSocket> playerConnections = new HashMap<>();

    public ServerRoom(RoomConfig config) {
        super(config);
    }

    /**
     * Añadir un nuevo jugador a la sala
     *
     * @param uuid       UUID del jugador
     * @param name       Nombre del jugador
     * @param connection Conexión Socket del jugador
     * @return La configuración que se le ha asignado
     * @throws Exception Si la sala está llena
     */
    public PlayerConfig addPlayer(String uuid, String name, WebSocket connection)
        throws Exception {
        if (!this.players.containsKey(uuid) && this.isFull()) {
            /// Si el jugador no estaba en la sala, y no hay hueco, lanzar excepción
            throw new Exception("Room is full.");
        }

        if (this.players.containsKey(uuid)) {
            /// Si estaba ya el jugador, solo hay que mandar la información de la sala otra vez
            /// Esto pasa cuando el jugador inicia una revancha, entra otra vez en la habitación al cambiar de interfaz
            broadCastRoomInfo();
            return this.players.get(uuid);
        }

        /// Tomar la primera configuración disponible para el jugador
        PlayerConfig playerConfig = roomConfig.getPlayerConfigs().get(0);
        roomConfig.getPlayerConfigs().remove(0);
        playerConfig.setName(name);

        /// Guardar su configuración, y la conexión Socket
        this.players.put(uuid, playerConfig);
        this.playerConnections.put(uuid, connection);
        ++connectedPlayers;

        /// Notificar a todos los jugadores conectados de que ha entrado un nuevo jugador
        broadCastRoomInfo();

        return playerConfig;
    }

    /**
     * Eliminar un jugador de la sala
     * <p>
     * Para cuando se desconecta
     *
     * @param uuid UUID del jugador
     */
    public void removePlayer(String uuid) {
        /// Si no estaba en la sala no hay que hacer nada
        if (!this.players.containsKey(uuid)) return;

        /// Obtener la configuración que le había asignado
        PlayerConfig playerConfig = this.players.get(uuid);

        /// Poner esta configuración en la cola, porque está disponible de nuevo
        this.roomConfig.getPlayerConfigs().add(playerConfig);

        /// Eliminar el jugador
        this.players.remove(uuid);
        this.playerConnections.remove(uuid);
        --connectedPlayers;

        /// Mandar un mensaje de rendirse a todos los jugadores
        /// No se tendría en cuenta este mensaje, salvo cuando están en la mitad de partida
        broadCast(new SurrenderCommand(uuid, null).toString(), null);

        broadCastRoomInfo();
    }

    /**
     * Enviar la información de la sala a todos los jugadores conectados
     */
    public void broadCastRoomInfo() {
        broadCast(new RoomInfoCommand(this).toString(), null);
    }

    /**
     * Método para mandar un mensaje a todos los jugadores conectados
     *
     * @param body      mensaje
     * @param _senderID El ID del emisor. Si el receptor es igual que emisor, no se envía
     */
    public void broadCast(String body, String _senderID) {
        for (Map.Entry<String, WebSocket> entry : this.playerConnections.entrySet()) {
            String clientID = entry.getKey();

            /// No mandar el mismo mensaje al emisor
            if (clientID.equals(_senderID)) continue;

            entry.getValue().send(body);
        }
    }
}
