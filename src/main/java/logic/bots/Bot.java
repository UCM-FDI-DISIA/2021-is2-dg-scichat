package logic.bots;

import exceptions.InvalidMoveException;
import exceptions.NotSelectedPieceException;
import exceptions.OccupiedCellException;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import logic.Board.Side;
import logic.Cell;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;
import utils.Mode;
import utils.PieceColor;

public class Bot implements Player {
    private Strategy strategy;
    private PieceColor color;
    private HashSet<Piece> pieces = new HashSet<>();
    private Side playerSide;
    private Piece selectedPiece = null;
    private boolean surrender;
    private String id;
    private long time;
    private boolean playing = false;
    private long timeAtTurnStart;
    boolean jumpIsLimited;
    private String name;

    public Bot(
        Strategy strat,
        PieceColor color,
        Side start,
        String id,
        boolean jumpIsLimited,
        String name
    )
        throws OccupiedCellException {
        this.strategy = strat;
        this.color = color;
        this.playerSide = start;
        this.id = id;
        this.time = 0;
        createPieces();
        this.jumpIsLimited = jumpIsLimited;
        this.name = name;
    }

    private void createPieces() throws OccupiedCellException {
        HashSet<Cell> in = this.playerSide.getSideCells();
        for (Cell i : in) {
            Piece aux = new Piece(i, this.color);
            pieces.add(aux);
        }
    }

    private void play() {
        strategy.move(this, jumpIsLimited);
    }

    public void move(Cell to, Mode mode)
        throws NotSelectedPieceException, InvalidMoveException {
        to = strategy.move(this, mode == Mode.Traditional);
        selectedPiece.move(to, mode);
    }

    @Override
    public void deselectPiece() {
        this.selectedPiece = null;
    }

    @Override
    public void endTurn() {
        this.time += System.currentTimeMillis() - this.timeAtTurnStart;
        playing = false;
    }

    @Override
    public PieceColor getColor() {
        return color;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        if (name == null) return id;
        return name;
    }

    @Override
    public Set<Piece> getPieces() {
        return pieces;
    }

    @Override
    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    @Override
    public Side getSide() {
        return playerSide;
    }

    @Override
    public boolean hasPiece(Piece piece) {
        return this.pieces.contains(piece);
    }

    @Override
    public boolean hasSelectedPiece() {
        return this.selectedPiece != null;
    }

    @Override
    public boolean hasSurrendered() {
        return surrender;
    }

    @Override
    public boolean isAWinner() {
        for (Piece pc : pieces) if (
            !playerSide.getOpposingCells().contains(pc.getPosition())
        ) {
            return false;
        }
        return true;
    }

    @Override
    public boolean selectPiece(Piece piece) {
        if (piece == null || !pieces.contains(piece)) return false;
        this.selectedPiece = piece;
        return true;
    }

    @Override
    public void softReset() {
        this.playing = false;
        this.selectedPiece = null;
        this.surrender = false;
        this.time = 0;

        this.pieces = new HashSet<Piece>();
        try {
            this.createPieces();
        } catch (OccupiedCellException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void startTurn() {
        this.timeAtTurnStart = System.currentTimeMillis();
        playing = true;
    }

    @Override
    public void surrender() {
        this.surrender = true;
    }

    @Override
    public long timePlaying() {
        if (playing) return (
            time + System.currentTimeMillis() - this.timeAtTurnStart
        ); else return time;
    }

    @Override
    public JSONObject toJSON() {
        JSONObject jPlayer = new JSONObject();
        jPlayer.put("color", this.color.getJSONValue());
        jPlayer.put("playerSide", this.playerSide.getJSONValue());
        jPlayer.put("surrender", this.surrender);
        jPlayer.put("id", this.id);
        jPlayer.put("time", this.time);
        jPlayer.put("playing", this.playing);
        jPlayer.put("timeATurnStart", this.timeAtTurnStart);

        JSONArray jPieces = new JSONArray();

        for (Piece piece : pieces) {
            jPieces.put(piece.toJSON());
        }
        jPlayer.put("pieces", jPieces);

        return jPlayer;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
