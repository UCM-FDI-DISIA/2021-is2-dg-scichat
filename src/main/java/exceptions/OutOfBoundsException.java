package exceptions;

public class OutOfBoundsException extends Exception {
    private static final long serialVersionUID = 5095894608330964025L;

    public OutOfBoundsException() {
        super("Posición fuera del tablero.");
    }

    public OutOfBoundsException(int row, int col) {
        super(String.format("La posición (%d,%d) está fuera del tablero.", row, col));
    }

    public OutOfBoundsException(String message) {
        super(message);
    }

    public OutOfBoundsException(Throwable cause) {
        super(cause);
    }

    public OutOfBoundsException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutOfBoundsException(
        String message,
        Throwable cause,
        boolean enableSuppression,
        boolean writeableStackTrace
    ) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
