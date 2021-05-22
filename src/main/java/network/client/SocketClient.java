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
    private String clientID;

    /// Se usa la clase CopyOnWriteArrayList para evitar problemas de modificación concurrente
    private final List<SocketObserver> observers = new CopyOnWriteArrayList<>();

    public SocketClient(URI serverUri) {
        super(serverUri);
        /// Método para configurar log4j, exigido por la librería de WebSocket que he usado
        /// No es importante
        BasicConfigurator.configure();
    }

    /**
     * Añadir observador
     *
     * @param o instancia de observador
     */
    public void addObserver(SocketObserver o) {
        if (this.observers.contains(o)) return;
        this.observers.add(o);
    }

    /**
     * Quitar observador
     *
     * @param o instancia de observador a quitar
     */
    public void removeObserver(SocketObserver o) {
        this.observers.remove(o);
    }

    /**
     * Si está conectado el Socket con el servidor
     */
    public boolean isConnected() {
        return this.connected;
    }

    /**
     * ID del cliente
     * <p>
     * Es diferente para cada conexión
     *
     * @return UUID del cliente
     */
    public String getClientID() {
        return clientID;
    }

    /**
     * Evento para cuando se establece la conexión de forma correcta
     *
     * @param serverHandshake
     */
    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        this.connected = true;
        for (SocketObserver o : this.observers) o.onOpen();
    }

    /**
     * Cuando recibe un mensaje del servidor
     *
     * @param s mensaje
     */
    @Override
    public void onMessage(String s) {
        /// Parsear el mensaje como JSONObject, y emitir a los observadores
        System.out.println("Mensaje recibido: " + s);
        try {
            JSONObject res = new JSONObject(s);
            String type = res.getString("type");
            if (type.equals("SET_CLIENT_ID")) {
                /// Configurar el ID del cliente
                this.clientID = res.getJSONObject("data").getString("clientID");
                this.onClientIDChange(clientID);
            } else if (type.equals("ERROR")) {
                /// Se ha producido un error en el servidor
                this.onError(
                        new Exception(res.getJSONObject("data").getString("message"))
                    );
            } else {
                /// Resto tipo de mensajes
                for (SocketObserver o : this.observers) o.onMessage(res);
            }
        } catch (JSONException e) {
            System.out.println("Error: " + s + " no es un JSON valido");
            e.printStackTrace();
        }
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        System.out.println("Connection closed");
        this.connected = false;
        for (SocketObserver o : this.observers) o.onClose();
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Error: ");
        e.printStackTrace();
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
