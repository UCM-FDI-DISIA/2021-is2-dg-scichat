package exceptions;

public class InvalidOperationException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidOperationException() {
        super("Invalid operation.");
    }

    public InvalidOperationException(String message) {
        super(message);
    }

    public InvalidOperationException(Throwable cause) {
        super(cause);
    }

    public InvalidOperationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidOperationException(String message, Throwable cause,
                                     boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
