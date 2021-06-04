package LoadSaveGame;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import control.LoadSaveCaretaker;
import java.io.File;
import java.util.ArrayList;
import logic.Game;
import logic.gameObjects.Player;
import org.junit.Test;

public class SaveLoadTest {
    Game game;
    LoadSaveCaretaker caretaker;
    File file;

    @Test
    public void loadSaveGameNoChange() {
        this.game = new Game();
        this.caretaker = new LoadSaveCaretaker(game);
        this.file = new File("prueba.json");

        //Cargamos el juego para comparar
        assertDoesNotThrow(
            () -> {
                this.game = caretaker.loadGame(file);
            }
        );

        //Guardamos los valores para comparar después
        boolean bfStopped = game.getStopped();
        int bfcurrntePlayerIndex = game.getCurrentPlayerIndex();
        utils.Mode bfMode = game.getGameMode();
        ArrayList<Player> bfPlayers = game.getPlayers();

        assertDoesNotThrow(
            () -> {
                caretaker.saveGame(file);
            }
        );

        assertDoesNotThrow(
            () -> {
                this.game = caretaker.loadGame(file);
            }
        );

        //Comprobamos que los parámetros se han guardado y cargado bien

        assertTrue(
            bfStopped == this.game.getStopped() &&
            bfcurrntePlayerIndex == game.getCurrentPlayerIndex()
        );

        assertTrue(bfMode.equals(game.getGameMode()));

        //Comprobamos las piezas se han cargado bien
        for (int i = 0; i < this.game.getPlayers().size(); ++i) {
            bfPlayers.get(i).equals(this.game.getPlayers().get(i));
        }
    }
}
