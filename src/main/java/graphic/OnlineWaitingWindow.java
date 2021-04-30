package graphic;

import javax.swing.*;
import network.client.SocketClient;
import network.client.SocketObserver;
import org.json.JSONObject;

public class OnlineWaitingWindow extends JFrame implements SocketObserver {
    private final SocketClient connection;
    private final String roomID;

    OnlineWaitingWindow(SocketClient _connection, String _roomID) {
        super("Habitación #" + _roomID);
        this.connection = _connection;
        this.roomID = _roomID;

        this.connection.addObserver(this);

        this.connectToRoom();
        this.initGUI();
    }

    private void connectToRoom() {
        /// Mandar una petición al servidor para entrar en habitación
        JSONObject req = new JSONObject();
        req.put("type", "JOIN_ROOM");

        JSONObject data = new JSONObject();
        data.put("clientID", this.connection.getClientID());
        data.put("roomID", this.roomID);

        req.put("data", data);
        connection.send(req.toString());
    }

    private void initGUI() {
        JLabel roomIDLabel = new JLabel("Room ID: " + this.roomID);

        this.getContentPane().add(roomIDLabel);

        this.setVisible(true);
    }
}
