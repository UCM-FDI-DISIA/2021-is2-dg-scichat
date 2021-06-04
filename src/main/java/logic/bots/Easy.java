package logic.bots;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import logic.Board;
import logic.Cell;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;

public class Easy implements Strategy {

    @Override
    public Cell move(Player player, boolean jumpIsLimited, Board board) {
        Cell result = null;
        double minDistance = Integer.MAX_VALUE;
        Cell corner = board.getOppositeCornerCell(player.getSide());
        Set<Cell> visited = new HashSet<Cell>();
        // Se recorren las piezas del jugador
        for (Piece piece : player.getPieces()) {
            // Por cada pieza se guardan en una lista los vecinos
            List<Cell> neighbours = piece.getPosition().getNeighbours();
            // Se recorren los vecinos en busca del movimiento más cercano a la esquina contraria a una casilla
            // adyacente libre
            for (Cell neighbour : neighbours) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    // Si estas vacia la casilla, calculas su distancia a la esquina
                    if (neighbour.isEmpty()) {
                        double distanceToCorner = piece
                            .getPosition()
                            .getDistanceBetween(corner);
                        if (distanceToCorner < minDistance) {
                            minDistance = distanceToCorner;
                            result = neighbour;
                            player.selectPiece(piece);
                            player.setLastMovement(piece.getPosition());
                        }
                    }
                }
            }
        }
        return result;
    }

    public String toString() {
        return "easy";
    }
}
