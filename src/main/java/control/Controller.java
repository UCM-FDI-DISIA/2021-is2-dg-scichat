package control;

import control.commands.Command;
import control.commands.CommandGenerator;
import logic.Game;

import java.util.Scanner;

public class Controller {
    public final String prompt = "Command > ";

    private Game game;
    private Scanner scanner;

    public Controller(Game game, Scanner scanner) {
        this.game = game;
        this.scanner = scanner;
    }

    public void printGame() {
        System.out.println(this.game);
    }

    public void run() {
        boolean refreshDisplay = true;

        while (!game.isFinished()) {
            if (refreshDisplay) printGame();
            refreshDisplay = false;

            System.out.println(prompt);
            String s = scanner.nextLine();
            String[] parameters = s.toLowerCase().trim().split("\\s+");
            System.out.println("[DEBUG] Executing: " + s);

            try {
                Command command = CommandGenerator.parse(parameters);
                refreshDisplay = command.execute(game);
            } catch (Exception ex) {
                System.out.format(ex.getMessage() + "%n%n");
            }
        }

        if (refreshDisplay) printGame();
        System.out.println("[GAME OVER]");
    }
}
