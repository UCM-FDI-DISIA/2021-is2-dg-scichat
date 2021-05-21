package logic.bots;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import logic.Board;
import logic.Board.Side;
import logic.Cell;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;
import utils.GeoComp;
import utils.GeoComp.Point;
import utils.GeoComp.Poligono;

public class Hard implements Strategy {
    private final int MAX_DIST = 16;
    private final int MAX_AREA = 144;
    private final int MAX_SYM = 56;
    private final int MAX_RANK = 1;

    private final double IMP_DIST = 10;
    private final double IMP_DIAM = 2;
    private final double IMP_SPARSE = 0; // 3;
    private final double IMP_SYM = 5;
    private final double IMP_RANK = 10;

    private double rankMove(Cell from, Cell to, Player player) {
        // Ranks move depending on if they start or end in the target triangle
        // In → In or Out → Out ranks 0.5
        // Out → In ranks 1
        // In → Out ranks 0
        Set<Cell> targets = player.getSide().getOpposingCells();
        boolean fromOn = targets.contains(from), toOn = targets.contains(to);
        if (fromOn == toOn) return 0.5; // In → In or Out → Out
        else if (!fromOn && toOn) return 1; // Out → In
        else return 0; // In → Out
    }

    private double heuristic(
        Player player,
        Cell to,
        boolean jumpIsLimited,
        Board board,
        Piece piece
    ) {
        Cell dest = board.getOppositeCornerCell(
            player.getSide()
        ), from = piece.getPosition();

        double dist = to.getDistanceBetween(dest); // Max should be 17, normal 4

        double diam = 0; // Max should be 17, normal 4
        List<GeoComp.Point> puntosPiezas = new ArrayList<>();
        for (Piece p1 : player.getPieces()) {
            Cell c1 = p1.getPosition();
            if (p1 != piece) puntosPiezas.add(new GeoComp.Point(c1));
            for (Piece p2 : player.getPieces()) {
                Cell c2 = p2.getPosition();
                if (!p1.equals(p2)) diam = Math.max(diam, c1.getDistanceBetween(c2));
            }
        }
        puntosPiezas.add(new GeoComp.Point(to));

        double sparse;
        GeoComp.Poligono polig = new GeoComp.Poligono(puntosPiezas);
        sparse = (IMP_SPARSE == 0 ? 0 : polig.convexHull().area()); // Max should be 169, min 8

        double symmetry = 0;
        GeoComp.Line line = new GeoComp.Line(
            new GeoComp.Point(from),
            new GeoComp.Point(to)
        );
        for (Piece p : player.getPieces()) symmetry +=
            line.dist(new GeoComp.Point(p.getPosition()));
        // Max should be 68

        double rank = rankMove(piece.getPosition(), to, player);

        double dist_norm = (dist / MAX_DIST), diam_norm = (diam / MAX_DIST), sparse_norm =
            (sparse / MAX_AREA), symmetry_norm = (symmetry / MAX_SYM), rank_norm =
            (rank / MAX_RANK);
        System.out.println(
            "Distance factor: " +
            dist_norm +
            "\nDiameter factor: " +
            diam_norm +
            "\nSparse factor: " +
            sparse_norm +
            "\nSymmetry factor:" +
            symmetry_norm +
            "\nRank factor:" +
            rank_norm
        );
        return (
            dist_norm *
            IMP_DIST +
            diam_norm *
            IMP_DIST +
            sparse_norm *
            IMP_SPARSE +
            symmetry_norm *
            IMP_SYM +
            rank_norm *
            IMP_RANK
        );
    }

    @Override
    public Cell move(Player player, boolean jumpIsLimited, Board board) {
        Cell result = null;
        double bestScore = Double.MAX_VALUE; // Peor puntuación posible de un movimiento

        for (Piece piece : player.getPieces()) {
            // Consideramos los movimientos a una pieza adyacente
            for (Cell curr : piece.getPosition().getNeighbours()) {
                if (!curr.isEmpty()) continue;
                // Escoger la mejor
                double score = heuristic(player, curr, jumpIsLimited, board, piece);
                if (bestScore > score) {
                    bestScore = score;
                    result = curr;
                    player.selectPiece(piece);
                    player.setLastMovement(piece.getPosition());
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
                double score = heuristic(player, curr, jumpIsLimited, board, piece);
                if (bestScore > score) {
                    bestScore = score;
                    result = curr;
                    player.selectPiece(piece);
                    player.setLastMovement(piece.getPosition());
                }
            }
        }

        return result;
    }
}
