package exceptions;

public class SaveGameException extends Exception{
    private static final long serialVersionUID = 1L;

    public SaveGameException() {
        super("There was an error while saving the game.");
    }

    public SaveGameException(String message) {
        super(message);
    }

    public SaveGameException(Throwable cause) {
        super(cause);
    }

    public SaveGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public SaveGameException(String message, Throwable cause,
                                     boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
