package exceptions;

class TooManyPlayersException extends Exception{
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
        super(message,cause, enableSuppresion, writableStackTrace);
    }
}