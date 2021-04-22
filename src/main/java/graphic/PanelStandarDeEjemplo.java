package graphic;

import control.Controller;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import logic.Game;
import logic.gameObjects.Piece;

public class PanelStandarDeEjemplo extends JPanel implements GameObserver {

    public PanelStandarDeEjemplo(Controller ctrl) {
        ctrl.addObserver(this);
        initGUI();
    }

    private void initGUI() {
        // TODO montar el componente

    }
}
