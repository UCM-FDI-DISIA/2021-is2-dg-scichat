package network.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

public class Server extends WebSocketServer {
    private int port;
    private Map<String, Room> rooms = new HashMap<>();
    private BiMap<UUID, WebSocket> clients = HashBiMap.create();

    public Server(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket connection, ClientHandshake handshake) {
        /// Cuando se conecta un nuevo cliente, asignarle un UUID, y añadirlo a la lista de clientes
        UUID clientID = UUID.randomUUID();
        this.clients.put(clientID, connection);
        System.out.printf("Cliente conectado. UUID: %s \n", clientID);

        /// Enviar este ID al cliente
        connection.send(
            new JSONObject()
                .put("type", "SET_CLIENT_ID")
                .put("data", new JSONObject().put("clientID", clientID))
                .toString()
        );
    }

    @Override
    public void onClose(WebSocket connection, int code, String reason, boolean remote) {
        /// Cuando se desconecta, eliminar de la lista de clientes
        /// TODO: También actualizar el estado de la habitación donde se encuentre
        UUID clientID = this.clients.inverse().get(connection);
        this.clients.remove(clientID);

        System.out.printf("Cliente desconectado. UUID: %s \n", clientID);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(message);
        try {
            JSONObject parsed = new JSONObject(message);
            if (parsed.getString("type").equals("NEW_ROOM")) {
                RoomConfig roomConfig = new RoomConfig(parsed.getJSONObject("data"));
                String roomID = Room.generateRoomID();
                Room room = new Room(roomConfig);

                this.rooms.put(roomID, room);

                JSONObject data = roomConfig.toJSONObject();
                data.put("roomID", roomID);
                conn.send(
                    new JSONObject()
                        .put("type", "ROOM_CREATED")
                        .put("data", data)
                        .toString()
                );

                System.out.println("Se ha creado una nueva habitación: ");
                System.out.println(data);
            }
        } catch (JSONException e) {
            conn.send(new JSONObject().put("type", "INVALID_MESSAGE").toString());
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {}

    @Override
    public void onStart() {
        System.out.printf(
            "Se ha abierto la conexión de WebSocket en el puerto %s \n",
            this.port
        );
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
    }
}
