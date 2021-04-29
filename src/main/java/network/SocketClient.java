package network;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class SocketClient extends WebSocketClient {
    private boolean connected = false;
    private List<SocketObserver> observers = new ArrayList<>();

    public SocketClient(URI serverUri) {
        super(serverUri);
    }

    public void addObserver(SocketObserver o) {
        this.observers.add(o);
    }

    public void removeObserver(SocketObserver o) {
        this.observers.remove(o);
    }

    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        this.connected = true;
        for (SocketObserver o : this.observers) o.onOpen();
    }

    @Override
    public void onMessage(String s) {
        for (SocketObserver o : this.observers) o.onMessage(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        this.connected = false;
        for (SocketObserver o : this.observers) o.onClose();
    }

    @Override
    public void onError(Exception e) {
        for (SocketObserver o : this.observers) o.onError(e);
    }

    public static void main(String[] args) {
        try {
            SocketClient ws = new SocketClient(new URI("ws://localhost:8080"));
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
