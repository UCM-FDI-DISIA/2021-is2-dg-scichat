package control.options;

import logic.Game;

public abstract class Option {
    public final String title;

    protected Option(String title) {
        this.title = title;
    }

    /**
     * Ejecutar la opción
     *
     * @param game referencia al game actual
     * @return devuelve true si se pasa del turno del jugador
     */
    public abstract boolean execute(Game game);
}
