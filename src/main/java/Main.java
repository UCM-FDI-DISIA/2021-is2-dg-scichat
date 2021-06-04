import control.Controller;
import javax.swing.*;
import logic.Game;

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
