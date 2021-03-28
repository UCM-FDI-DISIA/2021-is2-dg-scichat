package control.options;

import java.util.Scanner;

public class OptionGenerator {
    private static Option[] availableOptions = {

    };

    public static class ParseException extends Exception {
        public ParseException(String message) {
            super("[ERROR]: " + message);
        }
    }

    public static Option parse(Scanner scanner) throws ParseException {
        int optionIndex = -1;

        if (scanner.hasNextInt()) {
            /// Cargar la opción
            optionIndex = scanner.nextInt();
        }

        if (availableOptions.length > 0 && 1 <= optionIndex && optionIndex <= availableOptions.length) {
            /// Es una opción válida de las que hay
            return availableOptions[optionIndex - 1];
        }

        /// Opción no reconocida
        throw new ParseException("Opción no reconocida");
    }

    public static void printOptions() {
        System.out.println("Opciones disponibles: ");
        for (int i = 0; i < availableOptions.length; ++i) {
            System.out.format("     [%d]: %s \n", i + 1, availableOptions[i].title);
        }
    }
}
