package exceptions;

import logic.Cell;

public class CellsNotLinedUpException extends InvalidOperationException {

	private static final long serialVersionUID = 1L;

	public CellsNotLinedUpException() {
        super("Cells not lined up.");
    }
	
	public CellsNotLinedUpException(Cell c1, Cell c2) {
		super(String.format("Cells at %s and %s not lined up.",
				c1.toString(),c2.toString()));
	}

    public CellsNotLinedUpException(String message) {
        super(message);
    }

    public CellsNotLinedUpException(Throwable cause) {
        super(cause);
    }

    public CellsNotLinedUpException(String message, Throwable cause) {
        super(message, cause);
    }

    public CellsNotLinedUpException(String message, Throwable cause,
                                boolean enableSuppression, boolean writeableStackTrace) {
        super(message, cause, enableSuppression, writeableStackTrace);
    }
}
