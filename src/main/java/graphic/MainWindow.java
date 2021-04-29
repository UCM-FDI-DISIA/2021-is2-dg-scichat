package graphic;

import control.Controller;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import logic.Game;
import logic.gameObjects.Player;

//Se encargara de la interfaz dejando toda la logica interna a interacciones con ctrl
public class MainWindow extends JFrame implements GameObserver {
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
    private JPanel selectFileScreen = null;

    public MainWindow(Controller ctrl) {
        super("Damas Chinas");
        this.ctrl = ctrl;
        ctrl.addObserver(this); // Necesitamos incluirlo como observador para que pueda darse cuenta de que la
        // partida ha terminado
        initGUI();
    }

    private void initGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initStart();
        this.setVisible(true);
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
        //TODO crear pantalla gameScreen
        if (gameScreen == null) {
            gameScreen = new JPanel(new BorderLayout());
            gameScreen.add(new BoardPanel(ctrl), BorderLayout.LINE_START);
            gameScreen.add(new OptionsPanel(ctrl), BorderLayout.LINE_END);
            ctrl.addObserver(this);
        } else {
            ctrl.softReset();
        }
        this.setContentPane(gameScreen);
        this.pack();
        this.setSize(width, height);
        ctrl.initGame();
    }

    public void initWinner(Player winner) {
        //TODO crear pantalla winnerScreen
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
        this.gameOptionsScreen.open();

        Game newGame = new Game();
        newGame.setPlayers(gameOptionsScreen.getPlayers());
        newGame.setGameMode(gameOptionsScreen.getGameMode());
        ctrl.setGame(newGame);
        initGame();
    }

    public void initSelectFile() {
        if (this.loadGameScreen == null) this.loadGameScreen =
            new LoadGameWindow(ctrl, this);
        this.loadGameScreen.open();
    }

    public void onGameEnded(Game game) {
        //TODO
        initWinner(game.getWinner());
    }
}
