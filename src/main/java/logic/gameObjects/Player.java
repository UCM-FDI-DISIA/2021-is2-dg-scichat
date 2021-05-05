package logic.gameObjects;

import java.util.Set;

import org.json.JSONObject;

import exceptions.InvalidMoveException;
import exceptions.NotSelectedPieceException;
import logic.Board.Side;
import logic.Cell;
import utils.Mode;
import utils.PieceColor;

public interface Player {
    void deselectPiece();
    void endTurn();
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
    void startTurn();
    void surrender();
    long timePlaying();
    JSONObject toJSON();
    String toString();
}
