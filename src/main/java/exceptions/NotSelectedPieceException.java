package exceptions;

public class NotSelectedPieceException extends Exception {
    private static final long serialVersionUID = 1L;

    public NotSelectedPieceException() {
        super("No hay una pieza seleccionada.");
    }

    public NotSelectedPieceException(String arg0) {
        super(arg0);
    }

    public NotSelectedPieceException(Throwable arg0) {
        super(arg0);
    }

    public NotSelectedPieceException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public NotSelectedPieceException(
        String arg0,
        Throwable arg1,
        boolean arg2,
        boolean arg3
    ) {
        super(arg0, arg1, arg2, arg3);
    }
}
