package logic.gameObjects;

import exceptions.InvalidMoveException;
import exceptions.NotSelectedPieceException;
import java.util.Set;
import logic.Board.Side;
import logic.Cell;
import org.json.JSONObject;
import utils.Mode;
import utils.PieceColor;

public interface Player {
    void deselectPiece();
    PieceColor getColor();
    String getId();
    String getName();
    Set<Piece> getPieces();
    Piece getSelectedPiece();
    Side getSide();
    boolean hasPiece(Piece piece);
    boolean hasSelectedPiece();
    boolean hasSurrendered();
    boolean isAWinner();
    void move(Cell to, Mode mode) throws NotSelectedPieceException, InvalidMoveException;
    boolean selectPiece(Piece p);
    void softReset();
    void surrender();
    JSONObject toJSON();
    String toString();
    void setName(String name);
}
