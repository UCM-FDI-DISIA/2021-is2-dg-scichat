package graphic;

import java.awt.*;
import java.util.Map;
import javax.swing.*;
import logic.Board;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.commands.Command;
import network.commands.CommandParser;
import network.commands.RoomInfoCommand;
import network.models.PlayerConfig;
import network.models.Room;
import network.models.SocketMessage;
import org.json.JSONObject;
import utils.Mode;
import utils.PieceColor;

public class OnlineWaitingWindow extends JFrame implements SocketObserver {
    private final SocketClient connection;
    private final String roomID;
    private final JPanel roomInfoSection = new JPanel();
    private Room room;

    Command roomInfoCommand = new RoomInfoCommand("ROOM_INFO") {

        @Override
        public SocketMessage execute(JSONObject data, SocketClient connection) {
            Room _room = (Room) super.execute(data, connection);
            room = _room;

            renderRoomInfo();

            return _room;
        }
    };
    private CommandParser commandParser = new CommandParser() {

        @Override
        public Command[] getCommands() {
            return new Command[] { roomInfoCommand };
        }
    };

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
        JPanel container = new JPanel(new BorderLayout(10, 20));
        this.setContentPane(container);
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topSection = new JPanel();
        topSection.setLayout(new BoxLayout(topSection, BoxLayout.X_AXIS));

        JLabel roomIDLabel = new JLabel("Room ID: " + this.roomID);
        topSection.add(roomIDLabel);

        JPanel actionsSection = new JPanel();
        actionsSection.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton disconnectButton = new JButton("Desconectar");
        disconnectButton.addActionListener(
            e -> {
                this.connection.close();
                this.dispose();
            }
        );

        JButton startGameButton = new JButton("Empezar el juego");
        startGameButton.setEnabled(false);
        actionsSection.add(startGameButton);

        actionsSection.add(disconnectButton);

        container.add(topSection, BorderLayout.NORTH);
        container.add(roomInfoSection, BorderLayout.CENTER);
        container.add(actionsSection, BorderLayout.PAGE_END);

        this.pack();
        this.setVisible(true);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
    }

    private void renderRoomInfo() {
        if (this.room == null) return;

        this.roomInfoSection.removeAll();
        this.roomInfoSection.setLayout(new BorderLayout(10, 10));

        int connectedPlayers = this.room.getConnectedPlayers();
        int maxPlayers = this.room.getRoomConfig().getNumPlayers();
        Map<String, PlayerConfig> players = this.room.getPlayers();

        Mode mode = this.room.getRoomConfig().getMode();

        JPanel topSection = new JPanel(new GridLayout(3, 1));
        JLabel modeLabel = new JLabel("Modo del juego: " + mode);
        JLabel connectedPlayersLabel = new JLabel(
            "Jugadores conectados: " + connectedPlayers
        );
        topSection.add(modeLabel);
        topSection.add(connectedPlayersLabel);

        JProgressBar progressBar = new JProgressBar();
        topSection.add(progressBar);

        progressBar.setVisible(true);
        int value = connectedPlayers * 100 / maxPlayers;
        progressBar.setValue(value);
        progressBar.setStringPainted(true);
        progressBar.setString(
            String.format("%d / %d jugadores conectados", connectedPlayers, maxPlayers)
        );

        JPanel centerSection = new JPanel(new GridLayout(connectedPlayers, 1));

        for (Map.Entry<String, PlayerConfig> it : players.entrySet()) {
            String clientID = it.getKey();
            PlayerConfig playerConfig = it.getValue();

            JPanel playerSection = new JPanel(new FlowLayout());

            JLabel clientIDLabel = new JLabel("ID: " + clientID);

            PieceColor color = playerConfig.color;
            Board.Side side = playerConfig.side;

            playerSection.add(clientIDLabel);
            playerSection.add(new JLabel(color.toString()));
            playerSection.add(new JLabel(side.toString()));

            centerSection.add(playerSection);
        }

        roomInfoSection.add(topSection, BorderLayout.NORTH);
        roomInfoSection.add(centerSection, BorderLayout.CENTER);

        this.pack();
    }

    @Override
    public void onMessage(JSONObject s) {
        String type = s.getString("type");
        try {
            Command command = commandParser.parse(type);
            command.execute(s.getJSONObject("data"), this.connection);
        } catch (Exception e) {}
    }

    @Override
    public void onError(Exception e) {
        JOptionPane.showMessageDialog(
            this,
            e.getMessage(),
            "Error!",
            JOptionPane.ERROR_MESSAGE
        );
    }
}
