package graphic;

import java.awt.*;
import javax.swing.*;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.client.commands.Command;
import network.client.commands.CommandParser;
import org.json.JSONObject;

public class OnlineWaitingWindow extends JFrame implements SocketObserver {
    private final SocketClient connection;
    private final String roomID;
    private JEditorPane s = new JEditorPane();

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
        JPanel mainContent = new JPanel();
        this.setContentPane(mainContent);

        mainContent.setBackground(Color.WHITE);
        mainContent.setLayout(new GridBagLayout());

        JPanel container = new JPanel(new BorderLayout());
        mainContent.add(container);

        JLabel roomIDLabel = new JLabel("Room ID: " + this.roomID);

        container.add(roomIDLabel, BorderLayout.PAGE_START);
        container.add(s, BorderLayout.CENTER);

        this.setVisible(true);
    }

    Command RoomInfoCommand = new Command("ROOM_INFO") {

        @Override
        public void execute(JSONObject body, SocketClient connection) {
            /// Si recibe una información de la habitación, actualizar el GUI
            s.setText(body.toString());
        }
    };

    @Override
    public void onMessage(JSONObject s) {
        CommandParser commandParser = new CommandParser() {

            @Override
            public Command[] getCommands() {
                return new Command[] { RoomInfoCommand };
            }
        };

        String type = s.getString("type");
        try {
            Command command = commandParser.parse(type);
            command.execute(s, this.connection);
        } catch (Exception e) {}
    }
}
