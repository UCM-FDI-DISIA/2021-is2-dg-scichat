package logic.bots;

import java.util.List;
import java.util.Set;
import logic.Cell;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;

public class Normal implements Strategy {

    @Override
    public void decideMove(Player player) {
        Cell corner;
        int min = Integer.MAX_VALUE;
        corner = player.getSide().getCornerCell(player.getSide().getValue());
        for (Piece piece : player.getPieces()) {
            if (piece.isMovable()) {}
        }
    }
}
