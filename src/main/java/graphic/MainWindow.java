package graphic;

import control.Controller;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import logic.Game;
import logic.gameObjects.Player;
import network.client.SocketClient;
import network.models.Room;

//Se encargara de la interfaz dejando toda la logica interna a interacciones con ctrl
public class MainWindow extends JFrame implements GameObserver {
    private static final long serialVersionUID = 1L;

    private Controller ctrl;

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

    //Datos online
    private String roomID = null;
    private SocketClient connection = null;
    private String playerName = null;

    public MainWindow(Controller ctrl) {
        super("Damas Chinas");
        this.ctrl = ctrl;
        initGUI();
    }

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
        roomID = onlineConnectScreen.getRoomID();
        connection = onlineConnectScreen.getConnection();
        playerName = onlineConnectScreen.getName();
    }

    public void initOnlineWaiting() {
        onlineWaitingScreen =
            new OnlineWaitingWindow(this, connection, roomID, playerName, ctrl);
    }

    public void initOnlineGame(Room room) {
        onlineGameScreen = new OnlineGameWindow(this, ctrl, connection, roomID, room);
        onlineGameScreen.addLocalPlayer(connection.getClientID());
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
        if (startScreen == null) startScreen = new WelcomeWindow(this);
        this.setContentPane(startScreen);
        this.pack();
        this.setSize(width, height);
    }

    public void initGame() {
        if (gameScreen != null) {
            ctrl.softReset();
        }
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

    public void onGameEnded(Game game) {
        initWinner(game.getWinner());
    }
}
