package graphic;

import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;
import logic.gameObjects.Player;
import network.client.SocketClient;
import network.client.SocketObserver;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Mode;

public class OnlineConnectWindow extends JFrame implements SocketObserver {
    SocketClient sc;
    JButton newOnlineRoomButton;
    JButton joinOnlineRoomButton;
    JButton connectButton;
    JPanel actionsSection = new JPanel();

    public OnlineConnectWindow() {
        super("Juego Online");
        initGUI();
        setVisible(true);
    }

    private void initGUI() {
        this.getContentPane().setLayout(new GridBagLayout());
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout(0, 10));

        TitledBorder titleBorder = new TitledBorder("Juego Online");
        container.setBorder(
            new CompoundBorder(
                titleBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
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

        JTextField serverURLField = new JTextField("ws://localhost:8080");

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
                    URI serverURL = new URI(value);

                    /// Intentar conectar
                    this.sc = new SocketClient(serverURL);
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

        container.add(serverSection, BorderLayout.CENTER);

        actionsSection.setVisible(false);
        actionsSection.setLayout(new BoxLayout(actionsSection, BoxLayout.X_AXIS));
        container.add(actionsSection, BorderLayout.SOUTH);

        this.newOnlineRoomButton = new JButton("Nueva habitación");
        this.joinOnlineRoomButton = new JButton("Entrar a una habitación");

        actionsSection.add(newOnlineRoomButton);
        actionsSection.add(joinOnlineRoomButton);

        newOnlineRoomButton.addActionListener(
            e -> {
                NewGameWindow newGameWindow = new NewGameWindow(this);
                newGameWindow.open();

                Mode gameMode = newGameWindow.getGameMode();
                ArrayList<Player> players = newGameWindow.getPlayers();

                JSONObject gameConfig = new JSONObject();
                gameConfig.put("mode", gameMode.ordinal());

                JSONArray playersConfigArray = new JSONArray();
                for (Player p : players) {
                    JSONObject playerConfig = new JSONObject();
                    playerConfig.put("color", p.getColor().ordinal());
                    playerConfig.put("side", p.getSide().ordinal());
                    playersConfigArray.put(playerConfig);
                }

                gameConfig.put("players", playersConfigArray);

                sc.send(
                    new JSONObject()
                        .put("type", "NEW_ROOM")
                        .put("data", gameConfig)
                        .toString()
                );
            }
        );

        this.setSize(new Dimension(400, 400));
        this.setResizable(false);
    }

    public static void main(String[] args) {
        new OnlineConnectWindow();
    }

    @Override
    public void onOpen() {
        System.out.println("Se ha abierto una conexión Socket");
        actionsSection.setVisible(true);
        connectButton.setText("Desconectar");
    }

    @Override
    public void onMessage(String s) {
        System.out.println("Se ha recibido un mensaje del Socket:");
        System.out.println(s);
    }

    @Override
    public void onClose() {
        System.out.println("Se ha cerrado la conexión Socket");
        actionsSection.setVisible(false);

        connectButton.setText("Conectar");
    }

    @Override
    public void onError(Exception e) {
        System.out.println("Se ha producido una excepción en Socket");
        e.printStackTrace();
    }
}
