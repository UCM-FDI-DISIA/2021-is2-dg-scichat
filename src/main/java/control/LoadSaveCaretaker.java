package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import logic.Game;
import org.json.JSONObject;
import org.json.JSONTokener;

public class LoadSaveCaretaker {
    Game originator;

    public LoadSaveCaretaker(Game originator) {
        this.originator = originator;
    }

    public void setOriginator(Game originator) {
        this.originator = originator;
    }

    public void saveGame(File file) {
        JSONObject jGame = originator.toJSON();

        try {
            FileWriter exit = new FileWriter(file);
            exit.append(jGame.toString());
            exit.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public Game loadGame(File file) {
        JSONObject jGame = null;
        try {
            jGame = new JSONObject(new JSONTokener(new FileInputStream(file)));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        originator = new Game(jGame);

        return originator;
    }
}
