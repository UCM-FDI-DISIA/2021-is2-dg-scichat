package exceptions;

public class InvalidMoveException extends InvalidOperationException {
    private static final long serialVersionUID = 1L;

    public InvalidMoveException() {
        super("Invalid Move.");
    }

    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException(Throwable cause) {
        super(cause);
    }

    public InvalidMoveException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMoveException(String message, Throwable cause,
                                boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
