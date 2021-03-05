package exceptions;

class OutOfBoundsException extends Exception{
    public OutOfBoundsException(){
        super("Position out of bounds.");
    }
    public OutOfBoundsException(int row,int col){
        super("Position (%d,%d) out of bounds.".format(row,col));
    }
    public OutOfBoundsException(String message){
        super(message);
    }
    public OutOfBoundsException(Throwable cause){
        super(cause);
    }
    public OutOfBoundsException(String message, Throwable cause){
        super(message,cause);
    }
    public OutOfBoundsException(String message, Throwable cause,
        boolean enableSuppression, boolean writeableStackTrace){
        super(message,cause, enableSuppresion, writableStackTrace);
    }

}