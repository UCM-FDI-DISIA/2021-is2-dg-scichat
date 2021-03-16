package control;

import control.commands.Command;
import control.commands.CommandGenerator;

import java.util.Scanner;

public class Controller {
    public final String prompt = "Command > ";
    public static final String unknownCommandMsg = "Unknown command";

    private Scanner scanner;

    public Controller(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printGame() {
        /// TODO: Imprimir el juego
        System.out.println();
    }

    public void run() {
        boolean refreshDisplay = true;

        /// TODO: Hay que cambiar la condición de este bucle cuando implementemos la clase Game, a algo como !game.isFinished()
        /// Ahora mismo esto es un bucle infinito
        boolean isFinished = false;

        while (!isFinished) {
            if (refreshDisplay) printGame();
            refreshDisplay = false;

            System.out.println(prompt);
            String s = scanner.nextLine();
            String[] parameters = s.toLowerCase().trim().split("\\s+");
            System.out.println("[DEBUG] Executing: " + s);

            try {
                Command command = CommandGenerator.parse(parameters);
                refreshDisplay = command.execute();
            } catch (Exception ex) {
                System.out.format(ex.getMessage() + "%n%n");
            }
        }

        if (refreshDisplay) printGame();
        System.out.println("[GAME OVER]");
    }
}
