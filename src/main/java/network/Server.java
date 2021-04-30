package network;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;

public class Server extends WebSocketServer {
    private int port;
    private Map<String, Room> rooms = new HashMap<>();

    public Server(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {}

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {}

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
            "Se ha abierto la conexión de WebSocket en el puerto %s",
            this.port
        );
    }

    public static void main(String[] args) {
        Server server = new Server(8080);
        server.start();
    }
}
