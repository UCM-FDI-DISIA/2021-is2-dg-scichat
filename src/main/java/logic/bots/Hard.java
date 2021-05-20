package logic.bots;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import logic.Board;
import logic.Cell;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;
import utils.GeoComp;
import utils.GeoComp.Poligono;

public class Hard implements Strategy {
    private final int MAX_DIST = 17;
    private final int MAX_AREA = 169;

    private final double IMP_DIST = 10;
    private final double IMP_DIAM = 3;
    private final double IMP_SPARSE = 4;

    private double heuristic(Player player, Cell to, boolean jumpIsLimited, Board board) {
        Cell dest = board.getOppositeCornerCell(player.getSide().getValue());
        double dist = to.getDistanceBetween(to); // Max should be 17, normal 4

        double diam = 0; // Max should be 17, normal 4
        List<GeoComp.Point> puntosPiezas = new ArrayList<>();
        for (Piece p1 : player.getPieces()) {
            Cell c1 = p1.getPosition();
            puntosPiezas.add(new GeoComp.Point(c1.getCol(), c1.getRow()));
            for (Piece p2 : player.getPieces()) {
                Cell c2 = p2.getPosition();
                if (!p1.equals(p2)) diam = Math.max(diam, c1.getDistanceBetween(c2));
            }
        }
        GeoComp.Poligono p = new GeoComp.Poligono(puntosPiezas);
        double sparse = (IMP_SPARSE == 0 ? 0 : p.convexHull().area()); // Max should be 169, min 8

        return (
            (dist * 100 / MAX_DIST) *
            IMP_DIST +
            (diam * 100 / MAX_DIST) *
            IMP_DIST +
            (sparse * 100 / MAX_AREA) *
            IMP_SPARSE
        );
    }

    @Override
    public Cell move(Player player, boolean jumpIsLimited, Board board) {
        Cell result = null;
        double bestScore = Double.MAX_VALUE; // Peor puntuación posible de un movimiento

        for (Piece piece : player.getPieces()) {
            // Consideramos los movimientos a una pieza adyacente
            for (Cell curr : piece.getPosition().getNeighbours()) {
                // Escoger la mejor
                double score = heuristic(player, curr, jumpIsLimited, board);
                if (bestScore > score) {
                    bestScore = score;
                    result = curr;
                }
            }

            // Procedemos a hacer una búsqueda en anchura del mejor movimiento
            Set<Cell> visited = new HashSet<>();
            Queue<Cell> queue = new LinkedList<>();
            queue.add(piece.getPosition());
            while (!queue.isEmpty()) {
                Cell curr = queue.poll();
                visited.add(curr);

                // Añadimos los posibles siguientes saltos
                for (Cell neigh : Cell.getLargeJumpPositions(curr, jumpIsLimited)) if (
                    !visited.contains(neigh)
                ) queue.add(neigh);

                // Escoger la mejor
                double score = heuristic(player, curr, jumpIsLimited, board);
                if (bestScore > score) {
                    bestScore = score;
                    result = curr;
                }
            }
        }

        return result;
    }
}
