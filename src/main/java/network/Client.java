package network;

import java.net.URI;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Client extends WebSocketClient {

    public Client(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        System.out.println("Connected to server");
    }

    @Override
    public void onMessage(String s) {
        System.out.println("Received message: ");
        JSONObject json = new JSONObject(new JSONTokener(s));
        System.out.println(json);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Disconnected");
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e);
    }

    public static void main(String[] args) {
        try {
            Client ws = new Client(new URI("ws://localhost:8080"));
            ws.connect();

            /// Cierra la conexión al salir del programa
            Runtime
                .getRuntime()
                .addShutdownHook(new Thread(() -> ws.close(), "Shutdown-thread"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
