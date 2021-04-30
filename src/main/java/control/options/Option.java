package control.options;

import java.util.Scanner;
import logic.Game;

public abstract class Option {
    public final String title;

    /**
     * Excepción que se lanza cuando produce un error en ejecución
     */
    public static class ExecuteException extends Exception {
        private static final long serialVersionUID = 1L;

        public ExecuteException(String message) {
            super("[ERROR]: " + message);
        }
    }

    protected Option(String title) {
        this.title = title;
    }

    /**
     * Ejecutar la opción
     *
     * @param game    referencia al game actual
     * @param scanner referencia al scanner
     * @return devuelve true si se pasa del turno del jugador
     */
    public abstract boolean execute(Game game, Scanner scanner) throws ExecuteException;
}
