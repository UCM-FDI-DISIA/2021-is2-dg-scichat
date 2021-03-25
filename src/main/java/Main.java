import control.Controller;
import logic.Color;
import logic.Game;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    private static class SetupWizard {
        private static final String asciiLogo = "       __                                   __    _                 \n" +
                "  ____/ /___ _____ ___  ____ ______   _____/ /_  (_)___  ____ ______\n" +
                " / __  / __ `/ __ `__ \\/ __ `/ ___/  / ___/ __ \\/ / __ \\/ __ `/ ___/\n" +
                "/ /_/ / /_/ / / / / / / /_/ (__  )  / /__/ / / / / / / / /_/ (__  ) \n" +
                "\\__,_/\\__,_/_/ /_/ /_/\\__,_/____/   \\___/_/ /_/_/_/ /_/\\__,_/____/  \n" +
                "                                                                    ";

        private Scanner scanner;
        private int numPlayers;

        private SetupWizard(Scanner _scanner) {
            this.scanner = _scanner;
        }


        public void run() {
            printWelcome();
            setNumPlayers();

            setPlayers();
        }

        private void printWelcome() {
            System.out.println(asciiLogo);
            System.out.println("Pulsa cualquier tecla para empezar...");

            try {
                System.in.read();
            } catch (Exception e) {
            }

            clearConsole();
        }

        private void setNumPlayers() {
            numPlayers = -1;

            while (!(
                    numPlayers == 2 ||
                            numPlayers == 3 ||
                            numPlayers == 4 ||
                            numPlayers == 6
            )) {

                System.out.println("Introduce el número de jugadores: ");
                numPlayers = scanner.nextInt();
            }
        }


        private void setPlayers() {
            System.out.println("Configuración de jugadores");
            System.out.println("--------------------------");

            for (int i = 0; i < numPlayers; ++i) {
                System.out.println("Jugador [" + (i + 1) + "]");
                System.out.println("Colores disponibles: ");
                for(Color color: Color.values()){
                    System.out.print(color + " ");
                }
                System.out.println();
            }
        }
    }

    private static void clearConsole() {
        final String operatingSystem = System.getProperty("os.name");

        try {
            if (operatingSystem.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException e) {

        }
    }


    public static void main(String[] args) {
        SetupWizard s = new SetupWizard(scanner);

        s.run();

        /// Crear un nuevo controlador y ejecutar
        Controller controller = new Controller(new Game(), scanner);
        controller.run();
    }
}