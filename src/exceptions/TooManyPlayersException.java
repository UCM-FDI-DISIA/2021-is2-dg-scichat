package exceptions;

public class TooManyPlayersException extends Exception{
	private static final long serialVersionUID = -3356826865195160689L;
	public TooManyPlayersException(){
        super("Too many players.");
    }
    public TooManyPlayersException(String message){
        super(message);
    }
    public TooManyPlayersException(Throwable cause){
        super(cause);
    }
    public TooManyPlayersException(String message, Throwable cause){
        super(message,cause);
    }
    public TooManyPlayersException(String message, Throwable cause,
        boolean enableSuppression, boolean writeableStackTrace){
        super(message,cause, enableSuppression, writeableStackTrace);
    }
}