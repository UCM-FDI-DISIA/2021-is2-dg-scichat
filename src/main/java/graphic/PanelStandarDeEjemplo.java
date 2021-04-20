package graphic;

import control.Controller;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import logic.Game;

public class PanelStandarDeEjemplo extends JPanel implements GameObserver {

    public PanelStandarDeEjemplo(Controller ctrl) {
        ctrl.addObserver(this);
        initGUI();
    }

    private void initGUI() {
        // TODO montar el componente

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
