package logic.bots;

import java.util.HashSet;

import logic.Board;
import logic.Cell;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;

public class Normal implements Strategy {

    @Override
    public Cell move(Player player, boolean jumpIsLimited, Board board) {
        double minDistance = Integer.MAX_VALUE;
        Cell result = null;
        // Calculas la esquina contraria
        Cell corner = board.getOppositeCornerCell(player.getSide().getValue());
        // Recorres las piezas calculando su movimiento más cercano a la esquina
        for (Piece piece : player.getPieces()) {
            Cell currentClosestMovement = piece
                .getPosition()
                .getClosestMovementTo(corner, jumpIsLimited);
            // Si existe algun movimiento desde la pieza actual lo evaluas, si no pasas a la
            // ficha siguiente
            if (currentClosestMovement != null) {
                double distanceToCorner = currentClosestMovement.getDistanceBetween(
                    corner
                );
                // Si la distancia entre el movimiento de la pieza actual y la esquina es menor
                // que la distancia del movimiento más cercano a la esquina
                // guardas el destino, actualizas la distancia minima y seleccionas la pieza
                // cuyo movimiento es el más cercano
                if (distanceToCorner < minDistance) {
                    result = currentClosestMovement;
                    minDistance = distanceToCorner;
                    player.selectPiece(piece);
                    player.setLastMovement(piece.getPosition());
                }
            }
        }

        return result;
    }
}
