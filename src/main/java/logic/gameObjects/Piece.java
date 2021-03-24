package logic.gameObjects;

import exceptions.InvalidMoveException;
import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import exceptions.OutOfBoundsException;
import logic.Cell;
import logic.Color;

import java.util.Iterator;

public class Piece {

    // El color es lo que hace que una ficha pertenezca a un jugador
    private final Color color;
    private Cell position;

    public Color getColor() {
        return color;
    }

    public Piece(Cell pos, Color color) throws OccupiedCellException {
        this.color = color;

        if (!pos.isEmpty())
            throw new OccupiedCellException(pos);
        this.position = pos;
        this.position.putPiece(this);
    }

    public Cell getPosition() {
        return this.position;
    }

    public void tryToMoveTo(Cell targetPosition) throws InvalidOperationException, OutOfBoundsException {
        // Comprobamos que nos podemos mover hacia la posición dada
        // Para ello, tiene que haber una (Y solo una) ficha en la
        // posición intermedia, y la posición dada tiene que estar
        // en la misma diagonal que la posición actual

        if (targetPosition.isEmpty()) {
            if (!position.getNeighbours().contains(targetPosition)) {
                Iterator<Cell> it = this.position.getIteratorTowards(targetPosition);
                Cell mid = this.position.getMiddleCellTowards(targetPosition);

                while (it.hasNext()) {
                    Cell curr = it.next();
                    if (curr != mid && !curr.isEmpty())
                        throw new InvalidMoveException("There are cells obstructing the way.");
                    if (curr == mid && curr.isEmpty()) {
                        throw new InvalidMoveException("There is no cell in the middle.");
                    }
                }
            }
            // else → Queremos mover la pieza a uno de los adyacentes, que está vacio. Se
            // puede
        } else
            throw new InvalidOperationException("The target cell needs to be empty.");
    }

    /**
     * Desplazar la pieza
     *
     * @param targetPosition posición destino
     * @throws InvalidOperationException puede ser que sea una posición ocupada o un movimiento inválido
     * @throws OutOfBoundsException
     */
    public void move(Cell targetPosition) throws InvalidMoveException {
        try {
            tryToMoveTo(targetPosition);
            this.position.removePiece();
            this.position = targetPosition;
            this.position.putPiece(this);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new InvalidMoveException("The move is not possible.");
        }
    }


}