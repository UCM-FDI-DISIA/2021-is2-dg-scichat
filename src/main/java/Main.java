import control.Controller;
import exceptions.OccupiedCellException;
import logic.Board;
import logic.Game;

import java.awt.*;
import java.util.Queue;
import java.util.*;

public class Main {
    private static Scanner scanner = new Scanner(System.in);

    private static class SetupWizard {
        private static final String asciiLogo = "       __                                   __    _                 \n" +
                "  ____/ /___ _____ ___  ____ ______   _____/ /_  (_)___  ____ ______\n" +
                " / __  / __ `/ __ `__ \\/ __ `/ ___/  / ___/ __ \\/ / __ \\/ __ `/ ___/\n" +
                "/ /_/ / /_/ / / / / / / /_/ (__  )  / /__/ / / / / / / / /_/ (__  ) \n" +
                "\\__,_/\\__,_/_/ /_/ /_/\\__,_/____/   \\___/_/ /_/_/_/ /_/\\__,_/____/  \n" +
                "                                                                    ";

        private static final String Separator = "--------------------------";

        private Scanner scanner;
        private int numPlayers;
        private Game game = new Game();
        private ArrayList<Color> availableColors = new ArrayList<>();
        private Queue<Board.Side> availableSides = new LinkedList();

        private SetupWizard(Scanner _scanner) {
            this.scanner = _scanner;

            /// Inicialmente todos los colores disponibles
            this.availableColors.add(Color.GREEN);
            this.availableColors.add(Color.YELLOW);
            this.availableColors.add(Color.ORANGE);
            this.availableColors.add(Color.RED);
            this.availableColors.add(Color.MAGENTA);
            this.availableColors.add(Color.BLUE);
        }


        public void run() {
            printWelcome();
            selectGame();
        }

        private void printWelcome() {
            System.out.println(asciiLogo);
            System.out.print("Pulsa tecla <enter> para empezar...");

            try {
                System.in.read();
            } catch (Exception ignored) {
            }

            System.out.println(Separator);
        }

        /**
         * Mostrar menú para seleccionar si empezar una partida nueva, o cargar
         * <p>
         * (El nombre del método puede ser discutible)
         */
        private void selectGame() {
            Runnable displayMessage = () -> {
                System.out.println("Selecciona una opción: ");
                System.out.println();
                System.out.println("    [1]: Empezar una partida nueva");
                System.out.println("    [2]: Cargar una partida antigua");
                System.out.println();
                System.out.print("Opción: ");
            };

            displayMessage.run();
            int option = -1;

            if (scanner.hasNextInt()) {
                option = scanner.nextInt();
            }

            while (!(option == 1 || option == 2)) {
                /// Pedir que introduzca una opción válida mientras que no lo sea
                System.out.println("Opción no válida.");
                System.out.println(Separator);
                displayMessage.run();

                option = scanner.nextInt();
            }

            if (option == 1) {
                this.newGame();
            } else {    /// Opción 2 sería
                this.loadGame();
            }
        }

        /**
         * Método que inicializa la configuración de un nuevo juego
         */
        private void newGame() {
            setNumPlayers();
            setPlayers();
        }

        /**
         * Método para cargar un juego guardado
         */
        private void loadGame() {

        }

        private void setNumPlayers() {
            System.out.print("Introduce el número de jugadores (2, 3, 4, 6): ");

            if (scanner.hasNextInt()) {
                numPlayers = scanner.nextInt();
            }

            while (!(
                    numPlayers == 2 ||
                            numPlayers == 3 ||
                            numPlayers == 4 ||
                            numPlayers == 6
            )) {

                System.out.print("Número de jugadores inválido, vuelve a introducir (2, 3, 4, 6): ");

                if (scanner.hasNextInt()) {
                    numPlayers = scanner.nextInt();
                }
            }

            this.setAvailableSides();
        }

        private void printAvailableColors() {
            System.out.println("> Colores disponibles: ");
            System.out.println();

            for (int j = 0; j < this.availableColors.size(); ++j) {
                System.out.format("     [%d]: %s \n", j + 1, this.availableColors.get(j));
            }

            System.out.println();
        }

        private void setPlayers() {
            System.out.println(Separator);
            System.out.println("Configuración de jugadores");
            System.out.println(Separator);

            for (int i = 0; i < numPlayers; ++i) {
                /// Con método poll, saca el primer elemento de la cola de Sides por asignar, y lo elimina
                Board.Side side = this.availableSides.poll();
                System.out.format("Jugador [%d] - Posición [%s] \n", i + 1, side);
                this.printAvailableColors();

                System.out.format("> Color para jugador %d: ", i + 1);
                int colorInt = scanner.nextInt();

                while (!(colorInt >= 1 && colorInt <= this.availableColors.size())) {
                    System.out.println("> Color inválido. Vuelve a elegir");

                    this.printAvailableColors();

                    System.out.format("> Color para jugador %d: ", i + 1);
                    colorInt = scanner.nextInt();
                }

                Color color = this.availableColors.get(colorInt - 1);
                /// Se ha elegido un color, crear el nuevo jugador
                try {
                    /// Añadir el jugador, y quitar el color de la lista
                    this.game.addNewPlayer(color, side);
                    this.availableColors.remove(colorInt - 1);
                } catch (OccupiedCellException e) {
                    /// No va a lanzar nunca esta excepción, teóricamente
                }

                System.out.format("Se ha añadido correctamente el jugador [%d] - Color [%s] - Posición [%s] \n", i + 1, color, side);
                System.out.println();
            }
        }

        private void setAvailableSides() {
            /// Cargar los lados del tablero jugable, dependiendo del número de jugadores
            switch (this.numPlayers) {
                case 2:
                    /// Entonces juega en arriba y abajo
                    this.availableSides.add(Board.Side.Up);
                    this.availableSides.add(Board.Side.Down);
                    break;
                case 3:
                    /// Juega en triangulo invertido
                    this.availableSides.add(Board.Side.UpLeft);
                    this.availableSides.add(Board.Side.UpRight);
                    this.availableSides.add(Board.Side.Down);
                    break;
                case 4:
                    this.availableSides.add(Board.Side.UpLeft);
                    this.availableSides.add(Board.Side.UpRight);
                    this.availableSides.add(Board.Side.DownLeft);
                    this.availableSides.add(Board.Side.DownRight);
                    break;
                case 6:
                    /// Entonces todos los lados están disponibles
                    this.availableSides.addAll(Arrays.asList(Board.Side.values()));
                    break;
            }
        }

        public Game getGame() {
            /// Invocar este método una vez que termina de ejecutar run
            /// Entonces devuelve una instancia de Game configurada
            return game;
        }
    }

    public static void main(String[] args) {
        SetupWizard s = new SetupWizard(scanner);

        s.run();

        /// Crear un nuevo controlador y ejecutar
        Controller controller = new Controller(s.getGame(), scanner);
        controller.run();
    }
}