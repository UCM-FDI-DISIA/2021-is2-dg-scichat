package graphic;

import control.Controller;
import java.awt.BorderLayout;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import logic.Game;
import logic.gameObjects.HumanPlayer;
import logic.gameObjects.Player;
import network.client.SocketClient;
import network.models.Room;

//Se encargara de la interfaz dejando toda la logica interna a interacciones con ctrl
public class MainWindow extends JFrame implements GameObserver {
    private static final long serialVersionUID = 1L;

    private Controller ctrl;
    private SocketClient connection = null;

    //Datos de diseno
    public static int width = 930;
    public static int height = 700;

    //Pantallas principales
    private JPanel startScreen = null;
    private JPanel gameScreen = null;
    private JPanel winnerScreen = null;
    private NewGameWindow gameOptionsScreen = null;
    private LoadGameWindow loadGameScreen = null;
    private LocalServerSelectionScreen localOnlineScreen = null;
    private OnlineConnectWindow onlineConnectScreen = null;
    private OnlineWaitingWindow onlineWaitingScreen = null;
    private OnlineGameWindow onlineGameScreen = null;

    public MainWindow(Controller ctrl) {
        super("Damas Chinas");
        this.ctrl = ctrl;
        initGUI();
    }

    //Metodos para inicializar pantallas y ventanas
    private void initGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initStart();
        this.setVisible(true);
    }

    public void initLocalServer() {
        if (this.localOnlineScreen == null) localOnlineScreen =
            new LocalServerSelectionScreen(this);
        localOnlineScreen.open();
    }

    public void initOnlineConnect() {
        onlineConnectScreen = new OnlineConnectWindow(this);
        if (!onlineConnectScreen.open()) {
            closeConnection();
        }
        else {
            connection = onlineConnectScreen.getConnection();
            initOnlineWaiting();
        }
    }

    public void initOnlineWaiting() {
        String roomID = onlineConnectScreen.getRoomID();
        String playerName = onlineConnectScreen.getName();
        onlineWaitingScreen =
            new OnlineWaitingWindow(this, connection, roomID, playerName, ctrl);
        if (!onlineWaitingScreen.open()) {
            closeConnection();
        }
        else {
            initOnlineGame(onlineWaitingScreen.getRoom());
        }
    }

    public void initOnlineGame(Room room) {
        String roomID = onlineConnectScreen.getRoomID();
        onlineGameScreen = new OnlineGameWindow(ctrl, connection, roomID, room, this);
        onlineGameScreen.addLocalPlayer(connection.getClientID());
        onlineGameScreen.start();
        ctrl.addObserver(this);
        this.setContentPane(onlineGameScreen);
        this.pack();
        this.setSize(width, height);
    }

    public void initStart() {
        //No queremos que el juego anterior influya en el nuevo
        if (gameScreen != null) {
            ctrl.reset();
            gameScreen = null;
        }
        if (onlineGameScreen != null) {
            ctrl.reset();
            closeConnection();
            onlineConnectScreen = null;
            onlineWaitingScreen = null;
            onlineGameScreen = null;
        }
        if (startScreen == null) startScreen = new WelcomeWindow(this);
        this.setContentPane(startScreen);
        this.pack();
        this.setSize(width, height);
    }

    public void initGame() {
        gameScreen = new JPanel(new BorderLayout());
        gameScreen.add(new BoardPanel(ctrl), BorderLayout.LINE_START);
        gameScreen.add(new OptionsPanel(ctrl), BorderLayout.LINE_END);
        ctrl.addObserver(this);
        this.setContentPane(gameScreen);
        this.pack();
        this.setSize(width, height);
        ctrl.initGame();
    }

    public void initWinner(Player winner) {
        winnerScreen = new FinishedWindow(this, winner, ctrl);
        this.setContentPane(winnerScreen);
        this.pack();
        this.setSize(width, height);
        System.out.println("Ha ganado el jugador " + winner.getId());
    }

    public void initGameOptions() {
        if (this.gameOptionsScreen == null) {
            this.gameOptionsScreen = new NewGameWindow(this);
        }
        if (this.gameOptionsScreen.open() == 1) {
            Game newGame = new Game();
            newGame.setPlayers(gameOptionsScreen.getPlayers());
            newGame.setGameMode(gameOptionsScreen.getGameMode());
            ctrl.setGame(newGame);
            initGame();
        }
    }

    public void initSelectFile() {
        if (this.loadGameScreen == null) this.loadGameScreen =
            new LoadGameWindow(ctrl, this);
        this.loadGameScreen.open();
    }

    //Otros metodos
    public void closeConnection() {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public void initRematch() {
        if (connection != null) {
            try {
		connection = new SocketClient(new URI(onlineConnectScreen.getURL()));
	    } catch (URISyntaxException e) {
		JOptionPane.showMessageDialog(
                    this,
                    "Error al reestablecer conexion con el servidor",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
		initStart();
	    }
            connection.connect();
            initOnlineWaiting();
        } else {
            ctrl.softReset();
            initGame();
        }
    }

    public void onGameEnded(Game game) {
	if(connection!=null)connection.close();
        initWinner(game.getWinner());
    }
}
