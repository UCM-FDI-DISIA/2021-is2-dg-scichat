package control.options;

import java.util.Scanner;

public class OptionGenerator {
    private static final Option[] availableOptions = {
        new MovePieceOption("Mover una pieza"),
        new SurrenderOption("Rendirse"),
        new SaveGameOption("Guardar juego"),
        new LoadGameOption("Cargar juego"),
        new HelpOption("Ayuda")
    };

    public static class ParseException extends Exception {
        private static final long serialVersionUID = 1L;

        public ParseException(String message) {
            super("[ERROR]: " + message);
        }
    }

    public static Option parse(Scanner scanner) throws ParseException {
        int optionIndex = -1;

        System.out.print("> Opción: ");
        if (scanner.hasNextInt()) {
            /// Cargar la opción
            optionIndex = scanner.nextInt();
        }

        if (
            availableOptions.length > 0 &&
            1 <= optionIndex &&
            optionIndex <= availableOptions.length
        ) {
            /// Es una opción válida de las que hay
            return availableOptions[optionIndex - 1];
        }

        /// Opción no reconocida
        throw new ParseException("Opción no reconocida");
    }

    public static void printOptions() {
        System.out.println("Opciones disponibles: ");
        System.out.println();
        for (int i = 0; i < availableOptions.length; ++i) {
            System.out.format("     [%d]: %s \n", i + 1, availableOptions[i].title);
        }
        System.out.println();
    }
}
