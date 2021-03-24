import control.Controller;
import logic.Game;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /// Crear un nuevo controlador y ejecutar
        Controller controller = new Controller(new Game(), new Scanner(System.in));
        controller.run();
    }
}