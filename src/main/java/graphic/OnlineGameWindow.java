package graphic;

import control.Controller;

import java.awt.*;
import javax.swing.*;

import network.client.SocketClient;
import network.client.SocketObserver;

public class OnlineGameWindow extends JFrame implements SocketObserver, GameObserver {
    private Controller ctrl;
    private SocketClient sc;

    OnlineGameWindow(Controller _ctrl, SocketClient _sc) {
        this.sc = _sc;
        this.sc.addObserver(this);
        this.ctrl = _ctrl;
        this.ctrl.addObserver(this);
        this.initGUI();
    }

    protected void initGUI() {
        try{
            JPanel gameScreen = new JPanel(new BorderLayout());
            gameScreen.add(new BoardPanel(ctrl), BorderLayout.LINE_START);
            gameScreen.add(new OptionsPanel(ctrl), BorderLayout.LINE_END);
            this.setContentPane(gameScreen);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        this.pack();
        this.setVisible(true);
    }
}
