package control.options;

import logic.Game;

import java.util.Scanner;

public abstract class Option {
    public final String title;

    /**
     * Excepción que se lanza cuando produce un error en ejecución
     */
    public static class ExecuteException extends Exception {
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
