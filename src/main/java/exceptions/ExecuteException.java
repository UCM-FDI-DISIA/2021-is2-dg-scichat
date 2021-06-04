package exceptions;

public class ExecuteException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public ExecuteException(String message) {
        super("[ERROR]: " + message);
    }
}
