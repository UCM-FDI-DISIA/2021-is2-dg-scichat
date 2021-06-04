package graphic;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import logic.gameObjects.Player;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.commands.Command;
import network.commands.CommandParser;
import network.commands.CreateRoomCommand;
import network.models.PlayerConfig;
import network.models.RoomConfig;
import org.json.JSONObject;
import utils.Mode;

public class OnlineConnectWindow extends JDialog implements SocketObserver {
    SocketClient sc;
    JButton newOnlineRoomButton;
    JButton joinOnlineRoomButton;
    JButton connectButton;
    JPanel actionsSection = new JPanel();
    JLabel clientIDLabel = new JLabel();
    JPanel clientConfigSection;
    JTextField serverURLField;

    String name;
    private String roomID;
    JLabel playerNameLabel = new JLabel();

    private MainWindow parent = null;
    private boolean status;

    Command roomCreatedCommand = new Command("ROOM_CREATED") {

        @Override
        public void parseRequest(JSONObject req) {
            roomID = req.getJSONObject("data").getString("roomID");
        }

        @Override
        public void execute(JSONObject req, SocketClient connection) {
            super.execute(req, connection);
            sc.removeObserver(OnlineConnectWindow.this);
            status = true;
            dispose();
        }
    };

    CommandParser commandParser = new CommandParser() {

        @Override
        public Command[] getCommands() {
            return new Command[] { roomCreatedCommand };
        }
    };

    public String getRoomID() {
        return roomID;
    }

    public String getName() {
        return name;
    }

    public SocketClient getConnection() {
        return sc;
    }

    public String getURL() {
        return this.serverURLField.getText();
    }

    public OnlineConnectWindow(MainWindow parent) {
        super(parent, "Juego Online", true);
        this.parent = parent;
        initGUI();
    }

    private void initGUI() {
        JPanel mainContent = new JPanel();
        mainContent.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContent.setLayout(new GridBagLayout());
        this.setContentPane(mainContent);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout(0, 10));

        TitledBorder titleBorder = new TitledBorder("Juego Online");
        container.setBorder(
            new CompoundBorder(
                titleBorder,
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
            )
        );

        this.getContentPane().add(container);

        this.getContentPane().setBackground(Color.WHITE);
        container.setBackground(Color.WHITE);

        JPanel serverSection = new JPanel();
        serverSection.setFont(
            new Font(serverSection.getFont().getName(), Font.PLAIN, 20)
        );
        serverSection.setLayout(new BoxLayout(serverSection, BoxLayout.X_AXIS));

        serverURLField = new JTextField("ws://localhost:8080");

        serverSection.add(serverURLField);

        connectButton = new JButton("Conectar");
        serverSection.add(connectButton);

        connectButton.addActionListener(
            e -> {
                if (sc != null && sc.isConnected()) {
                    sc.close();
                    return;
                }

                String value = serverURLField.getText();
                if (value.isEmpty()) return;

                try {
                    this.sc = parent.createConnection(value);
                    this.sc.addObserver(this);
                    this.sc.connect();
                } catch (URISyntaxException uriSyntaxException) {
                    uriSyntaxException.printStackTrace();
                    JOptionPane.showMessageDialog(
                        this,
                        value + " no es una dirección correcta",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        );

        container.add(serverSection, BorderLayout.NORTH);

        actionsSection.setVisible(false);
        actionsSection.setLayout(new GridLayout(1, 2));
        container.add(actionsSection, BorderLayout.CENTER);

        this.newOnlineRoomButton = new JButton("Nueva habitación");
        this.joinOnlineRoomButton = new JButton("Entrar a una habitación");

        actionsSection.add(newOnlineRoomButton);
        actionsSection.add(joinOnlineRoomButton);

        //TODO
        newOnlineRoomButton.addActionListener(
            e -> {
                NewGameWindow newGameWindow = new NewGameWindow(parent, false);
                if (newGameWindow.open() == 0) return;

                Mode gameMode = newGameWindow.getGameMode();
                ArrayList<Player> players = newGameWindow.getPlayers();
                LinkedList<PlayerConfig> playerConfigList = new LinkedList<>();

                for (Player p : players) {
                    PlayerConfig playerConfig = new PlayerConfig(
                        p.getColor(),
                        p.getSide()
                    );
                    playerConfigList.add(playerConfig);
                }

                RoomConfig roomConfig = new RoomConfig(gameMode, playerConfigList);
                new CreateRoomCommand(roomConfig).send(sc);
            }
        );

        joinOnlineRoomButton.addActionListener(
            e -> {
                roomID =
                    JOptionPane.showInputDialog(
                        this,
                        "Introduce código de la habitación: "
                    );
                if (roomID.isEmpty()) return;

                roomID = roomID.toUpperCase();

                this.sc.removeObserver(this);

                status = true;
                dispose();
            }
        );

        clientConfigSection = new JPanel(new GridLayout(2, 1));
        clientConfigSection.setAlignmentX(Component.LEFT_ALIGNMENT);
        clientConfigSection.setBackground(Color.WHITE);
        clientConfigSection.setVisible(false);
        container.add(clientConfigSection, BorderLayout.SOUTH);

        clientConfigSection.add(clientIDLabel);

        JPanel nameConfigSection = new JPanel(new BorderLayout(30, 10));
        nameConfigSection.setBackground(Color.WHITE);
        clientConfigSection.add(nameConfigSection);

        playerNameLabel = new JLabel("Nombre: " + this.name);
        nameConfigSection.add(playerNameLabel, BorderLayout.WEST);

        JButton changeNameButton = new JButton("Cambiar");
        nameConfigSection.add(changeNameButton, BorderLayout.EAST);

        changeNameButton.addActionListener(
            e -> {
                String newName = JOptionPane.showInputDialog(this, "Nombre: ", this.name);
                if (newName == null || newName.isEmpty()) return;

                changeName(newName);
            }
        );

        this.setLocationRelativeTo(null);
        this.setMinimumSize(new Dimension(600, 300));
        this.pack();
    }

    private void changeName(String _name) {
        this.name = _name;
        this.playerNameLabel.setText("Nombre: " + this.name);
    }

    @Override
    public void onOpen() {
        System.out.println("Se ha abierto una conexión Socket");
        actionsSection.setVisible(true);
        clientConfigSection.setVisible(true);
        connectButton.setText("Desconectar");
        this.pack();
    }

    @Override
    public void onClientIDChange(String clientID) {
        this.clientIDLabel.setText("ID del cliente: " + clientID);
        if (this.name == null) changeName(clientID);
    }

    @Override
    public void onMessage(JSONObject s) {
        System.out.println("Se ha recibido un mensaje del Socket:");
        System.out.println(s);

        String type = s.getString("type");
        try {
            Command c = commandParser.parse(type);
            c.execute(s, this.sc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose() {
        System.out.println("Se ha cerrado la conexión Socket");
        actionsSection.setVisible(false);
        clientConfigSection.setVisible(false);

        this.clientIDLabel.setText("");
        connectButton.setText("Conectar");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Se ha producido una excepción en Socket");
        e.printStackTrace();
    }

    public boolean open() {
        status = false;
        setLocation(getParent().getLocation().x + 50, getParent().getLocation().y + 50);
        pack();
        setVisible(true);
        return status;
    }
}
