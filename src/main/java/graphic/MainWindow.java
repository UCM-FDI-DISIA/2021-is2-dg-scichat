package graphic;

import control.Controller;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import javax.swing.JFrame;
import logic.Board;
import logic.Game;
import logic.gameObjects.Player;

//Se encargara de la interfaz dejando toda la logica interna a interacciones con ctrl
public class MainWindow extends JFrame implements GameObserver {
    private Controller ctrl;

    public MainWindow(Controller ctrl) {
        super("Damas Chinas");
        this.ctrl = ctrl;
        initGUI();
    }

    private void initGUI() {
        ctrl.addObserver(this); //Necesitamos incluirlo como observador para que pueda darse cuenta de que la partida a terminado
        //TODO crear la ventana juntando los distintos componentes
    }

    @Override
    public void onRegister(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSelectedPiece(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onEndTurn(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSurrendered(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onReset(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGameEnded(Game game) {
        // TODO Auto-generated method stub

    }
}
