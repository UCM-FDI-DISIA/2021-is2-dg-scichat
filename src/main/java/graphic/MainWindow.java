package graphic;

import control.Controller;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import logic.Board;
import logic.Game;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;

//Se encargara de la interfaz dejando toda la logica interna a interacciones con ctrl
public class MainWindow extends JFrame implements GameObserver {
    private Controller ctrl;

    //Pantallas principales
    private JPanel startScreen;
    private JPanel gameScreen;
    private JPanel winnerScreen;
    private JPanel gameOptionsScreen;
    private JPanel selectFileScreen;

    public MainWindow(Controller ctrl) {
        super("Damas Chinas");
        this.ctrl = ctrl;
        ctrl.addObserver(this); //Necesitamos incluirlo como observador para que pueda darse cuenta de que la partida ha terminado
        initGUI();
    }

    private void initGUI() {
        initStart();
        initGameOptions();
        this.setContentPane(startScreen);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
    }

    private void initStart() {
        //TODO crear pantalla initScreen
    }

    private void initGame() {
        //TODO crear pantalla gameScreen
    }

    private void initWinner() {
        //TODO crear pantalla winnerScreen
    }

    private void initGameOptions() {
        //TODO crear pantalla gameOptionsScreen
    }

    private void initSelectFile() {
        //TODO crear pantalla selectFileScreen
    }
}
