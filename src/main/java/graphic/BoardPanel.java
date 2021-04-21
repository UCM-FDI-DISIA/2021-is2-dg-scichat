package graphic;

import control.Controller;
import java.awt.Dimension;
import java.util.HashMap;
import javax.swing.JPanel;
import logic.Board;
import logic.Cell;

public class BoardPanel extends JPanel implements GameObserver {
    private static final long serialVersionUID = -3501731254500743354L;
    private static final int default_radius = 20;

    private Controller ctrl = null;
    private HashMap<Cell, CellLabel> cellLabels = new HashMap<>();

    public BoardPanel(Controller ctrl) {
        this(ctrl, default_radius, null);
    }

    /**
     * Create a BoardPanel connected to controller with the data
     * of board.
     *
     * @param ctrl		controller to connect to
     * @param radius	radius of the CellLabels
     * @param board		board to load the data from
     */
    public BoardPanel(Controller ctrl, int radius, Board board) {
        this.ctrl = ctrl;

        int label_height = 2 * radius;
        int label_width = (int) (Math.sqrt(4.0 / 3) * label_height);

        initGUI(new Dimension(13 * label_width, 17 * label_height));
        this.setBoard(board, radius); // Puede que mejor mover esto a 	onRegister
    }

    private void initGUI(Dimension dim) {
        this.setLayout(null);
        this.setPreferredSize(dim);
    }

    public void setBoard(Board board, int radius) {
        if (board == null) return;
        cellLabels.clear();
        for (Cell cell : board) {
            CellLabel clabel = new CellLabel(this, cell, radius);
            cellLabels.put(cell, clabel); // Store in dictionary
            this.add(clabel); // Load in JPanel
        }
    }

    public void handleClick(Cell position) {
        this.ctrl.handleClick(position);
    }
}
