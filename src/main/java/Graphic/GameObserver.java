package Graphic;

import logic.Board;
import logic.gameObjects.Player;

public interface GameObserver {
    void onRegister(Board board, Player currentplayer);
    void onSelectedPiece(Board board, Player currentplayer);
    void onEndTurn(Board board, Player currentplayer);
    void onSurrendered(Board board, Player currentplayer);
    void onReset(Board board, Player currentplayer);
    void onRequestedRules(Board board, Player currentplayer);
}
