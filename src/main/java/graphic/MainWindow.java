package graphic;

import control.Controller;
import exceptions.OccupiedCellException;
import java.awt.BorderLayout;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import logic.Board;
import logic.Board.Side;
import logic.Game;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;
import utils.Mode;
import utils.PieceColor;

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
    private JPanel gameOptionsScreen = null;
    private JPanel selectFileScreen = null;

    public MainWindow(Controller ctrl) {
        super("Damas Chinas");
        this.ctrl = ctrl;
        ctrl.addObserver(this); //Necesitamos incluirlo como observador para que pueda darse cuenta de que la partida ha terminado
        initGUI();
    }

    private void initGUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initStart();
        this.setVisible(true);
    }

    private void initStart() {
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
	System.out.println("Ha ganado el jugador "+winner.getId());
    }

    public void initGameOptions() {
        //TODO crear pantalla gameOptionsScreen
        System.out.println("Ahora se abriria el panel de opciones de partida");
        //Creacion de game hasta implementacion de gameOptions
        Game g = new Game();
        try {
            g.addNewPlayer(PieceColor.BLUE, Side.Down);
            g.addNewPlayer(PieceColor.RED, Side.Up);

            g.addNewPlayer(PieceColor.GREEN, Side.DownLeft);
            g.addNewPlayer(PieceColor.MAGENTA, Side.UpRight);

            g.addNewPlayer(PieceColor.ORANGE, Side.DownRight);
            g.addNewPlayer(PieceColor.YELLOW, Side.UpLeft);
            g.setGameMode(Mode.Traditional);
        } catch (OccupiedCellException e) {
            e.printStackTrace();
        }
        ctrl.setGame(g);
        initGame();
    }

    public void initSelectFile() {
        //TODO crear pantalla selectFileScreen
        System.out.println("Ahora se abriria el panel de seleccionar partida");
    }

    public void onGameEnded(Game game) {
	//TODO
        initWinner(game.wonBySurrender());
    }
}
