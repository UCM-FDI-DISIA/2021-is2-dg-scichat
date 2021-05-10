package graphic;

import control.Controller;
import exceptions.OccupiedCellException;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;
import logic.Board;
import logic.Game;
import logic.gameObjects.HumanPlayer;
import logic.gameObjects.Player;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.commands.*;
import network.models.PlayerConfig;
import network.models.Room;
import org.json.JSONObject;
import utils.Mode;

public class OnlineWaitingWindow extends JDialog implements SocketObserver {
    private final SocketClient connection;
    private final String roomID;
    private final JPanel roomInfoSection = new JPanel();
    private Room room;
    private String name;
    private Controller ctrl;
    private boolean status;

    private JButton startGameButton;

    Command roomInfoCommand = new RoomInfoCommand() {

        @Override
        public void execute(JSONObject data, SocketClient connection) {
            super.execute(data, connection);
            OnlineWaitingWindow.this.room = this.getRoom();
            renderRoomInfo();
        }
    };

    Command startGameCommand;

    private CommandParser commandParser = new CommandParser() {

        @Override
        public Command[] getCommands() {
            return new Command[] { roomInfoCommand, startGameCommand };
        }
    };
    
    public Room getRoom() {
	return this.room;
    }

    OnlineWaitingWindow(
        MainWindow _parent,
        SocketClient _connection,
        String _roomID,
        String _name,
        Controller _ctrl
    ) {
        super(_parent, "Habitación #" + _roomID, true);
        this.setLocation(_parent.getX(), _parent.getY());
        this.connection = _connection;
        this.roomID = _roomID;
        this.name = _name;
        this.ctrl = _ctrl;

        this.connection.addObserver(this);

        this.connectToRoom();
        this.initGUI();

        this.startGameCommand =
            new StartGameCommand(roomID) {

                @Override
                public void execute(JSONObject data, SocketClient connection) {
                    connection.removeObserver(OnlineWaitingWindow.this);
                    createGame();
                    status = true;
                    setVisible(false);
                }
            };
    }

    private void connectToRoom() {
        new JoinRoomCommand(this.roomID, this.connection.getClientID(), name)
        .send(this.connection);
    }

    /// Crear el controlador con las configuraciones dadas
    private void createGame() {
        Game game = new Game();
        ctrl.setGame(game);

        game.setGameMode(room.getRoomConfig().getMode());

        ArrayList<Player> players = new ArrayList<>();

        for (Map.Entry<String, PlayerConfig> it : room.getPlayers().entrySet()) {
            PlayerConfig config = it.getValue();
            try {
                Player newPlayer = new HumanPlayer(
                    config.color,
                    config.side,
                    it.getKey()
                );
                newPlayer.setName(config.getName());
                players.add(newPlayer);
            } catch (OccupiedCellException e) {}
        }

        game.setPlayers(players);
    }

    private void initGUI() {
        JPanel container = new JPanel(new BorderLayout(10, 20));
        this.setContentPane(container);
        container.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topSection = new JPanel(new BorderLayout());

        JLabel roomIDLabel = new JLabel("Room ID: " + this.roomID);
        JButton copyRoomIDButton = new JButton("Copiar");
        copyRoomIDButton.addActionListener(
            e -> {
                StringSelection stringSelection = new StringSelection(this.roomID);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        );
        topSection.add(roomIDLabel, BorderLayout.WEST);
        topSection.add(copyRoomIDButton, BorderLayout.EAST);

        JPanel actionsSection = new JPanel();
        actionsSection.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JButton disconnectButton = new JButton("Desconectar");
        disconnectButton.addActionListener(
            e -> {
                this.connection.close();
                this.dispose();
            }
        );

        startGameButton = new JButton("Empezar el juego");
        startGameButton.setEnabled(false);
        startGameButton.addActionListener(
            e -> {
                startGameCommand.send(this.connection);
            }
        );

        actionsSection.add(startGameButton);

        actionsSection.add(disconnectButton);

        container.add(topSection, BorderLayout.NORTH);
        container.add(roomInfoSection, BorderLayout.CENTER);
        container.add(actionsSection, BorderLayout.PAGE_END);

        this.setResizable(false);
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
            PlayerConfig playerConfig = it.getValue();

            JPanel playerSection = new JPanel(new FlowLayout(FlowLayout.LEFT));

            playerSection.add(new Circle(20, playerConfig.color.getColor()));

            JLabel playerNameLabel = new JLabel(playerConfig.getName());
            Board.Side side = playerConfig.side;

            playerSection.add(playerNameLabel);
            playerSection.add(new JLabel(side.toString()));

            centerSection.add(playerSection);
        }

        roomInfoSection.add(topSection, BorderLayout.NORTH);
        roomInfoSection.add(centerSection, BorderLayout.CENTER);

        this.startGameButton.setEnabled(this.room.isFull());
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

    public boolean open() {
        status = false;
        this.pack();
        this.setVisible(true);
        return status;
    }
}
