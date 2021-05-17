package network.server;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import network.commands.*;
import network.models.ServerRoom;
import org.apache.log4j.BasicConfigurator;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONObject;

public class Server extends WebSocketServer {
    private int port;
    private Map<String, ServerRoom> rooms = new HashMap<>();
    private BiMap<String, WebSocket> clients = HashBiMap.create();

    private CommandParser commandParser = new CommandParser() {

        @Override
        public Command[] getCommands() {
            return new Command[] {
                new CreateRoomCommand(),
                new JoinRoomCommand(),
                new StartGameCommand(),
                new PieceMovedCommand(),
                new SurrenderCommand(),
                new RematchCommand()
            };
        }
    };

    public Server(int port) {
        super(new InetSocketAddress(port));
        this.port = port;
    }

    public Map<String, ServerRoom> getRooms() {
        return rooms;
    }

    public final ServerRoom getRoom(String roomID) throws Exception {
        if (!this.rooms.containsKey(roomID)) {
            throw new Exception("Room ID " + roomID + " does not exist.");
        }

        return this.rooms.get(roomID);
    }

    @Override
    public void onOpen(WebSocket connection, ClientHandshake handshake) {
        /// Cuando se conecta un nuevo cliente, asignarle un UUID, y añadirlo a la lista de clientes
        UUID clientID = UUID.randomUUID();
        this.clients.put(clientID.toString(), connection);
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
        String clientID = this.clients.inverse().get(connection);
        this.clients.remove(clientID);

        for (Map.Entry<String, ServerRoom> entry : this.rooms.entrySet()) {
            /// Buscar la habitación donde estaba ese jugador, y eliminarlo
            ServerRoom serverRoom = entry.getValue();
            serverRoom.removePlayer(clientID);
        }

        System.out.printf("Cliente desconectado. UUID: %s \n", clientID);
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println(message);
        try {
            JSONObject body = new JSONObject(message);
            Command command = commandParser.parse(body.getString("type"));
            command.execute(body.getJSONObject("data"), this, conn);
        } catch (Exception e) {
            e.printStackTrace();
            conn.send(
                new JSONObject()
                    .put("type", "ERROR")
                    .put("data", new JSONObject().put("message", e.getMessage()))
                    .toString()
            );
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
    }

    @Override
    public void onStart() {
        System.out.printf(
            "Se ha abierto la conexión de WebSocket en el puerto %s \n",
            this.port
        );
    }

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Server server = new Server(8080);
        server.start();
    }
}
