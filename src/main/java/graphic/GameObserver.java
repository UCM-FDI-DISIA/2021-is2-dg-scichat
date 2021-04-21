package graphic;

import logic.Game;
import logic.gameObjects.Piece;

//Esta debe de ser la interfaz que sigan todos los componentes visuales para que game pueda informarles de los cambios en el juego
public interface GameObserver {
    void onRegister(Game game);
    void onSelectedPiece(Piece piece);
    void onEndTurn(Game game);
    void onSurrendered(Game game);
    void onReset(Game game); //No se si al final vamos a hacer funcionalidad de terminar y volver a empezar
    void onGameEnded(Game game);
}
