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
    private JPanel startScreen=null;
    private JPanel gameScreen=null;
    private JPanel winnerScreen=null;
    private JPanel gameOptionsScreen=null;
    private JPanel selectFileScreen=null;

    public MainWindow(Controller ctrl) {
        super("Damas Chinas");
        this.ctrl = ctrl;
        ctrl.addObserver(this); //Necesitamos incluirlo como observador para que pueda darse cuenta de que la partida ha terminado
        initGUI();
    }

    private void initGUI() {
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	initStart();
    }
    
    private void initStart() {
	//TODO crear pantalla initScreen
	if(startScreen==null)
	    startScreen=new WelcomeWindow(this);
	this.setContentPane(startScreen);
	this.pack();
	this.setSize(900, 700);
	this.setVisible(true);
    }
    
    public void initGame() {
	//TODO crear pantalla gameScreen
    }
    
    public void initWinner() {
	//TODO crear pantalla winnerScreen
    }
    
    public void initGameOptions() {
	//TODO crear pantalla gameOptionsScreen
	System.out.println("Ahora se abriria el panel de opciones de partida");
    }
    
    public void initSelectFile() {
	//TODO crear pantalla selectFileScreen
	System.out.println("Ahora se abriria el panel de seleccionar partida");
    }
}
