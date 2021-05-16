package network.client;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.log4j.BasicConfigurator;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

public class SocketClient extends WebSocketClient {
    private boolean connected = false;
    private List<SocketObserver> observers = new CopyOnWriteArrayList<>();
    private String clientID;

    public SocketClient(URI serverUri) {
        super(serverUri);
        BasicConfigurator.configure();
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

    public String getClientID() {
        return clientID;
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        this.connected = true;
        for (SocketObserver o : this.observers) o.onOpen();
    }

    @Override
    public void onMessage(String s) {
        /// Parsear el mensaje como JSONObject, y emitir a los observadores
        try {
            JSONObject res = new JSONObject(s);
            String type = res.getString("type");
            if (type.equals("SET_CLIENT_ID")) {
                /// Configurar el ID del cliente
                this.clientID = res.getJSONObject("data").getString("clientID");
                this.onClientIDChange(clientID);
            } else if (type.equals("ERROR")) {
                this.onError(
                        new Exception(res.getJSONObject("data").getString("message"))
                    );
            } else {
                for (SocketObserver o : this.observers) o.onMessage(res);
            }
        } catch (JSONException e) {
            System.out.println("Error: " + s + " no es un JSON valido");
            e.printStackTrace();
        }
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

    public void onClientIDChange(String clientID) {
        for (SocketObserver o : this.observers) o.onClientIDChange(clientID);
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
