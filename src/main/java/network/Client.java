package network;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient {
    private List<SocketObserver> observers = new ArrayList<>();

    public Client(URI serverUri) {
        super(serverUri);
    }

    public void addObserver(SocketObserver o) {
        this.observers.add(o);
    }

    public void removeObserver(SocketObserver o) {
        this.observers.remove(o);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        for (SocketObserver o : this.observers) o.onOpen();
    }

    @Override
    public void onMessage(String s) {
        for (SocketObserver o : this.observers) o.onMessage(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        for (SocketObserver o : this.observers) o.onClose();
    }

    @Override
    public void onError(Exception e) {
        for (SocketObserver o : this.observers) o.onError(e);
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
