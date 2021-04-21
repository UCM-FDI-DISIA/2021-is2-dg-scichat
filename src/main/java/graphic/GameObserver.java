package graphic;

import logic.Cell;
import logic.Game;
import logic.gameObjects.Piece;

//Esta debe de ser la interfaz que sigan todos los componentes visuales para que game pueda informarles de los cambios en el juego
public interface GameObserver {
    default void onRegister(Game game) {}
    default void onSelectedPiece(Piece piece) {}
    default void onMovedPiece(Cell from, Piece piece) {}
    default void onEndTurn(Game game) {}
    default void onSurrendered(Game game) {}
    default void onReset(Game game) {} //No se si al final vamos a hacer funcionalidad de terminar y volver a empezar
    default void onGameEnded(Game game) {}
}
