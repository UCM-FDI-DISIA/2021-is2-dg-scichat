package logic.bots;

import exceptions.OccupiedCellException;
import java.util.HashSet;
import logic.Board.Side;
import logic.Cell;
import logic.gameObjects.Piece;
import utils.PieceColor;

public class Bot {
    private Strategy strategy;
    private PieceColor color;
    private HashSet<Piece> pieces = new HashSet<>();
    private Side playerSide;
    private Piece selectedPiece = null;
    private boolean surrender;
    private int id;
    private boolean playing = false;

    public Bot(Strategy strat, PieceColor color, Side start, int id)
        throws OccupiedCellException {
        this.strategy = strat;
        this.color = color;
        this.playerSide = start;
        this.id = id;
        createPieces();
    }

    private void createPieces() throws OccupiedCellException {
        HashSet<Cell> in = this.playerSide.getSideCells();
        for (Cell i : in) {
            Piece aux = new Piece(i, this.color);
            pieces.add(aux);
        }
    }

    private void play() {}
}
