package logic.bots;

import logic.Cell;
import logic.gameObjects.Player;

public interface Strategy {
    Cell move(Player player, boolean jumpIsLimited);
}
