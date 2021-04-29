package logic.gameObjects;

import exceptions.InvalidMoveException;
import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import exceptions.OutOfBoundsException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import logic.Board;
import logic.Cell;
import utils.Mode;
import utils.PieceColor;

public class Piece implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    // El color es lo que hace que una ficha pertenezca a un jugador
    private final PieceColor color;
    private Cell position;

    /* Constructor */
    public Piece(Cell pos, PieceColor color) throws OccupiedCellException {
        this.color = color;
        if (!pos.isEmpty()) throw new OccupiedCellException(pos);
        this.position = pos;
        this.position.putPiece(this);
    }

    /* Getters */

    public PieceColor getColor() {
        return color;
    }

    public Cell getPosition() {
        return this.position;
    }

    /**
     * Comprueba si se puede llevar a cabo un determinado movimiento
     *
     * @param targetPosition Celda a la que queremos mover la ficha
     * @throws InvalidOperationException Salta cuando el Movimiento que queremos
     *                                   llevar a cabo no es posible
     * @throws OutOfBoundsException      Salta cuando la posicion a moverse no es
     *                                   posible
     */
    public void tryToMoveTo(Cell targetPosition) throws InvalidMoveException {
        if (targetPosition.equals(this.position) || !targetPosition.isEmpty()) { // Comprobamos que la posición que queremos alcanzar no es la de
            // partida
            throw new InvalidMoveException();
        }

        List<Cell> neighbours = this.position.getNeighbours(); // Obtenemos los vecinos

        if (!neighbours.contains(targetPosition)) { // Si no encontramos la posición buscada entre los vecinos
            boolean[][] checkedCells = new boolean[Board.NUM_ROW][Board.NUM_COL]; // Creamos una matriz de booleanos que
            // utilizaremos como marcador para ver
            // que piezas ya hemos estudiado
            for (int i = 0; i < Board.NUM_ROW; ++i) { // Inicializamos la matriz
                for (int j = 0; j < Board.NUM_COL; ++j) checkedCells[i][j] = false;
            }

            Queue<Cell> positionQueue = new LinkedList<Cell>();
            positionQueue.add(this.position);

            if (!recursiveTryToMoveTo(targetPosition, checkedCells, positionQueue)) {
                throw new InvalidMoveException(
                    "No se puede acceder a la casilla " + targetPosition
                );
            }
        }
    }

    /**
     * Método recursivo que nos devuelve true si se puede alcancar la posición o no.
     * Para ello guardamos en una cola las posiciones que podemos alcanzar a través
     * de realizar saltos sobre otras fichas. Vamos sacando los elementos de la cola
     * y comprobando para cada una de esas posiciones los saltos que podemos
     * realizar desde cada una de ellas. Guardamos esos nuevos saltos en la cola de
     * posiciones para estudiarlas más adelante. Si no podemos realizar nuevos
     * saltos desde la posición actual, no podemos alcanzar la posición deseada
     * desde la actual posicion, devolvemos falso. El programa acaba cuando se haya
     * un camino hasta la posición deseada o cuando se han estudiado todos y ninguno
     * nos lleva a la misma.
     *
     * @param targetPosition  Posición que queremos alcanzar
     * @param checkedCells    Tablero con las Celdas que ya hemos mirado
     * @return Devuelve si se puede llevar a cabo el movimiento
     */
    private boolean recursiveTryToMoveTo(
        Cell targetPosition,
        boolean[][] checkedCells,
        Queue<Cell> positionQueue
    ) {
        Cell positionToCheck = positionQueue.peek(); // Accedemos a la siguiente posición a estudiar

        if (!checkedCells[positionToCheck.getRow()][positionToCheck.getCol()]) { // Comprobamos que no la hemos
            // estudiado ya
            positionQueue.poll(); // Retiramos la posición de la cola
            List<Cell> neighbours = positionToCheck.getNeighbours(); // Obtenemos sus vecinos

            for (Cell ady : neighbours) {
                Cell newJump = positionToCheck.getCellJump(ady); // Vemos el posible salto que podemos obtener desde la
                // posición actual y dado un vecio concreto
                if (newJump != null) {
                    if (newJump.equals(targetPosition)) return true; // deseada // Comprobamos si el nuevo salto nos proporciona la posición
                    positionQueue.add(newJump); // Añadimos la posición a la cola para estudiarla más adelante
                }
            }

            checkedCells[positionToCheck.getRow()][positionToCheck.getCol()] = true; // Actualizamos la matriz de
            // comprobaciones

            return recursiveTryToMoveTo(targetPosition, checkedCells, positionQueue); // Comprobamos las nuevas
            // posiciones
        }

        return false; // En caso de que no podamos realizar nuevos movimientos devolvemos false
    }

    /**
     * Comprueba si se puede llevar a cabo un determinado movimiento en juego rapido
     *
     * @param targetPosition Celda a la que queremos mover la ficha
     * @throws InvalidOperationException Salta cuando el Movimiento que queremos
     *                                   llevar a cabo no es posible
     * @throws OutOfBoundsException      Salta cuando la posicion a moverse no es
     *                                   posible
     */
    public void tryToMoveFast(Cell targetPosition) throws InvalidMoveException {
        // TODO todo
        if (targetPosition.equals(this.position)) { // La estamos ocupando!
            throw new InvalidMoveException();
        }

        Set<Cell> visited = new HashSet<>(); // Podría hacer una matriz de booleanos, pero
        // para no depender de NUM_COLS y NUM_ROWS hago esto
        Queue<Cell> childrenQueue = new LinkedList<Cell>();
        childrenQueue.add(this.position);
        while (!childrenQueue.isEmpty()) {
            Cell current = childrenQueue.poll(); // Como .front() y .pop() combinados

            if (current == targetPosition) return;

            visited.add(current);
            List<Cell> candidates = Cell.getLargeJumpPositions(current);
            for (Cell candidate : candidates) if (
                !visited.contains(candidate)
            ) childrenQueue.add(candidate);
        } // Si agotamos todos los candidatos, es que no se puede hacer un saltando otras
        // piezas
        tryToMoveTo(targetPosition);
    }

    /**
     * Desplazar la pieza, según el modo de juego
     *
     * @param targetPosition posición destino
     * @param playMode       modo de juego
     * @throws InvalidOperationException puede ser que sea una posición ocupada o un
     *                                   movimiento inválido
     * @throws OutOfBoundsException
     */
    public void move(Cell targetPosition, Mode playMode) throws InvalidMoveException {
        try {
            // Vemos que podemos llevar a cavo el movimiento
            if (playMode == Mode.Traditional) tryToMoveTo(targetPosition); else if (
                playMode == Mode.Fast
            ) tryToMoveFast(targetPosition);

            this.position.removePiece(); // Actualizamos las posiciones(celdas)
            this.position = targetPosition;
            this.position.putPiece(this);
        } catch (InvalidOperationException ioe) {
            System.out.println(ioe.getMessage());
            throw new InvalidMoveException("The move is not possible.", ioe);
        }
    }
}
