package logic.bots;

import java.util.*;
import logic.Board;
import logic.Cell;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;
import utils.GeoComp;

public class Hard implements Strategy {
    private final int MAX_DIST = 16;
    private final int MAX_AREA = 144;
    private final int MAX_SYM = 56;
    private final int MAX_RANK = 1;

    private final double IMP_DIST = 10;
    private final double IMP_DIST_ULT = 10;
    private final double IMP_DIAM = 2;
    private final double IMP_SPARSE = 0; // 3;
    private final double IMP_SYM = 5;
    private final double IMP_RANK = 10;

    private double rankMove(Cell from, Cell to, Player player) {
        // Ranks move depending on if they start or end in the target triangle
        // In -> In or Out -> Out ranks 0.5
        // Out -> In ranks 0
        // In -> Out ranks 1
        Set<Cell> targets = player.getSide().getOpposingCells();
        boolean fromOn = targets.contains(from), toOn = targets.contains(to);
        if (fromOn == toOn) return 0.5; // In -> In or Out -> Out
        else if (!fromOn && toOn) return 0; // Out -> In
        else return 1; // In -> Out
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
        List<Cell> finalPositions = new ArrayList<>();
        for (Piece p1 : player.getPieces()) if (p1 != piece) finalPositions.add(
            p1.getPosition()
        );
        finalPositions.add(to);

        double dist = to.getDistanceBetween(dest); // Max should be 17, normal 4

        double dist_ult = dist;
        for (Cell c : finalPositions) {
            dist_ult = Math.max(dist_ult, c.getDistanceBetween(dest));
        }

        double diam = 0; // Max should be 17, normal 4
        List<GeoComp.Point> puntosPiezas = new ArrayList<>();
        for (Cell c1 : finalPositions) {
            puntosPiezas.add(new GeoComp.Point(c1));
            for (Cell c2 : finalPositions) {
                if (!c1.equals(c2)) diam = Math.max(diam, c1.getDistanceBetween(c2));
            }
        }

        double sparse;
        GeoComp.Poligono polig = new GeoComp.Poligono(puntosPiezas);
        sparse = (IMP_SPARSE == 0 ? 0 : polig.convexHull().area());

        double symmetry = 0;
        GeoComp.Line line = new GeoComp.Line(
            new GeoComp.Point(
                board.getOppositeCornerCell(player.getSide().getOpposite())
            ),
            new GeoComp.Point(dest)
        );
        for (Cell c : finalPositions) symmetry += line.dist(new GeoComp.Point(c));

        double rank = rankMove(from, to, player);

        double dist_norm = (dist / MAX_DIST), dist_ult_norm =
            (dist_ult / MAX_DIST), diam_norm = (diam / MAX_DIST), sparse_norm =
            (sparse / MAX_AREA), symmetry_norm = (symmetry / MAX_SYM), rank_norm =
            (rank / MAX_RANK);
        System.out.println(
            "Distance factor: " +
            dist_norm +
            "\nDistance max factor:" +
            dist_ult_norm +
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
            dist_ult_norm *
            IMP_DIST_ULT +
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

    public String toString() {
        return "hard";
    }
}
