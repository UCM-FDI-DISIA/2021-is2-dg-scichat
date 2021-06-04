package network.server;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import logic.Board;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.commands.CreateRoomCommand;
import network.commands.JoinRoomCommand;
import network.models.PlayerConfig;
import network.models.RoomConfig;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import utils.Mode;
import utils.PieceColor;

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
    String addRoom() {
        /// Cola de respuestas esperadas por la parte del servidor
        Queue<String> expectedResponseType = new LinkedList<>();

        List<String> roomID = new ArrayList<>();

        SocketObserver observer = new SocketObserver() {

            @Override
            public void onMessage(JSONObject s) {
                System.out.println(s);
                String type = s.getString("type");
                String expected = expectedResponseType.poll();
                assertEquals(expected, type);

                roomID.add(s.getJSONObject("data").getString("roomID"));
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

        client.removeObserver(observer);

        return roomID.get(0);
    }

    /**
     * Probemos ahora a entrar en esa nueva sala
     *
     * @throws Exception
     */
    @Test
    void joinRoom() throws Exception {
        String newRoomID = addRoom();

        assertNotNull(server.getRoom(newRoomID));

        /// Cola de respuestas esperadas por la parte del servidor
        Queue<String> expectedResponseType = new LinkedList<>();

        SocketObserver observer = new SocketObserver() {

            @Override
            public void onMessage(JSONObject s) {
                String type = s.getString("type");
                String expected = expectedResponseType.poll();
                assertEquals(expected, type);
            }
        };

        client.addObserver(observer);

        /// Cuando entra en una sala, recibe su información
        expectedResponseType.add("ROOM_INFO");

        /// Entrar en la sala
        new JoinRoomCommand(newRoomID, "Hello").send(client);

        /// Esperar a que procese todas las respuestas
        await().until(() -> expectedResponseType.isEmpty());

        client.removeObserver(observer);
    }
}
