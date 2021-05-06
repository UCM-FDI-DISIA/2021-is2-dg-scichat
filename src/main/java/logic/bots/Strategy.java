package logic.bots;

import logic.Cell;
import logic.gameObjects.Player;

public interface Strategy {
    void decideMove(Player player);
}
