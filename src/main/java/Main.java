import control.Controller;
import exceptions.OccupiedCellException;
import java.util.*;
import javax.swing.*;
import logic.Board;
import logic.Game;
import utils.Mode;
import utils.PieceColor;

public class Main {


        /**
         * Método que inicializa la configuración de un nuevo juego
         */

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(
            new Runnable() {

                @Override
                public void run() {
                    new graphic.MainWindow(new Controller(new Game()));
                }
            }
        );
    }
}
