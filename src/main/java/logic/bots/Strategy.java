package logic.bots;

import logic.Board;
import logic.Cell;
import logic.gameObjects.Player;

public interface Strategy {
    /**
     * Dado un jugador y un tablero, selecciona la pieza a mover y devuelve
     * la posición a la que moverla según la estrategia
     *
     * @param player jugador para el que calcular el movimiento
     * @param jumpIsLimited decide si se permiten saltos de longitud mayor a 1
     * @param board tablero en el que mover la pieza
     * @return
     */
    Cell move(Player player, boolean jumpIsLimited, Board board);
}
