package logic.bots;

import java.util.List;
import logic.Cell;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;

public class Easy implements Strategy {

    @Override
    public Cell move(Player player, boolean jumpIsLimited) {
        Cell result = null;
        // Se recorren las piezas del jugador
        for (Piece piece : player.getPieces()) {
            // Por cada pieza se guardan en una lista los vecinos
            List<Cell> neighbours = piece.getPosition().getNeighbours();
            // Se recorren los vecinos en busca del primer movimiento a una casilla adyacente libre
            for (Cell neighbour : neighbours) {
                // Si se encuentra dicha casilla se guarda y se deja de buscar
                if (neighbour.isEmpty()) {
                    result = neighbour;
                    break;
                }
            }
            if (result != null) {
                break;
            }
        }
        return result;
    }
}
