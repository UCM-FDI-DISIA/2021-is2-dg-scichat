package network.server;

import logic.Board;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.commands.CreateRoomCommand;
import network.models.PlayerConfig;
import network.models.RoomConfig;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Mode;
import utils.PieceColor;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ServerTest {
    private static Server server;
    private static SocketClient client;

    /**
     * Crear el servidor antes de empezar cada test
     */
    @BeforeAll
    static void startServer() {
        server = new Server(8080);
        server.start();
    }

    @AfterAll
    static void closeServer() throws InterruptedException {
        server.stop();
    }

    /**
     * Preparar el cliente
     */
    @BeforeAll
    static void startClient() {
        try {
            client = new SocketClient(new URI("ws://localhost:8080"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        /// Conectar
        client.connect();

        /// Esperar que se conecta con éxito
        /// Fallaría el test si en 10s no se conecta
        await().until(() -> client.isConnected());
    }

    @AfterAll
    static void stopClient() {
        client.close();
    }

    /**
     * Comprobar que se puede crear la habitación
     */
    @Test
    void addRoom() {
        /// Cola de respuestas esperadas por la parte del servidor
        Queue<String> expectedResponseType = new LinkedList<>();

        SocketObserver observer = new SocketObserver() {
            @Override
            public void onMessage(JSONObject s) {
                System.out.println(s);
                String type = s.getString("type");
                String expected = expectedResponseType.poll();
                assertEquals(expected, type);
            }
        };

        client.addObserver(observer);

        /// Enviar una petición de crear la sala
        List<PlayerConfig> players = new ArrayList<>();
        players.add(new PlayerConfig(PieceColor.BLUE, Board.Side.Down));
        players.add(new PlayerConfig(PieceColor.RED, Board.Side.Up));
        RoomConfig roomConfig = new RoomConfig(Mode.Fast, players);

        /// Se espera que ahora, al enviar la petición, el servidor responda con una confirmación de sala creada
        expectedResponseType.add("ROOM_CREATED");
        new CreateRoomCommand(roomConfig).send(client);

        /// Esperar a que procese todas las respuestas
        await().until(() -> expectedResponseType.isEmpty());
    }
}
