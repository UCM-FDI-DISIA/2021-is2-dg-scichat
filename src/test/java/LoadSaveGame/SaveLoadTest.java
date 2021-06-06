package LoadSaveGame;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

import control.LoadSaveCaretaker;
import java.io.File;
import java.util.ArrayList;
import logic.Game;
import logic.gameObjects.Player;
import org.junit.Test;
import utils.Mode;

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
        boolean bfStopped = game.isFinished();
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
            bfStopped == this.game.isFinished() &&
            bfcurrntePlayerIndex == game.getCurrentPlayerIndex()
        );

        assertTrue(bfMode.equals(game.getGameMode()));

        //Comprobamos las piezas se han cargado bien
        for (int i = 0; i < this.game.getPlayers().size(); ++i) {
            assertTrue(
                bfPlayers.get(i).getSide().getValue() ==
                (this.game.getPlayers().get(i)).getSide().getValue()
            );
        }
    }

    @Test
    public void withChangeLoadSave() {
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
        boolean bfStopped = game.isFinished();
        int bfcurrntePlayerIndex = game.getCurrentPlayerIndex();
        utils.Mode bfMode = game.getGameMode();
        ArrayList<Player> bfPlayers = game.getPlayers();

        //Modificamos el juego
        this.game.reset();
        file = new File("guardarPrueba.json");

        //Guardamos y cargamos de nuevo el juego
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

        //Comprobamosque los datos son distintos
        assertTrue(bfcurrntePlayerIndex != game.getCurrentPlayerIndex());
        System.out.println(bfMode);
        System.out.println(game.getGameMode());

        assertTrue(game.getGameMode().equals(Mode.Fast)); // Valor por defecto

        //Comprobamos las piezas se han cargado bien, es decir que son distintas
        for (int i = 0; i < this.game.getPlayers().size(); ++i) {
            assertTrue(
                !(
                    bfPlayers.get(i).getSide().getValue() ==
                    (this.game.getPlayers().get(i)).getSide().getValue()
                )
            );
        }
    }
}
