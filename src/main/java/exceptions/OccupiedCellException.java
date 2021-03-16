package exceptions;

import logic.Cell;

public class OccupiedCellException extends InvalidOperationException {
	
	public OccupiedCellException() {
        super("Cell is occupied.");
    }
	
	public OccupiedCellException(Cell pos) {
		super(String.format("Cell %s is occupied.",pos.toString()));
	}

    public OccupiedCellException(String message) {
        super(message);
    }

    public OccupiedCellException(Throwable cause) {
        super(cause);
    }

    public OccupiedCellException(String message, Throwable cause) {
        super(message, cause);
    }

    public OccupiedCellException(String message, Throwable cause,
                                boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
