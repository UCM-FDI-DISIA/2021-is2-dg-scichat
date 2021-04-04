package control.options;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

import control.options.Option.ExecuteException;
import logic.Game;

public class SaveGameOption extends Option {

    protected SaveGameOption(String title) {
	super(title);
    }

    @Override
    public boolean execute(Game game, Scanner scanner) throws ExecuteException {

	System.out.println("Ingrese el nombre del archivo desde donde desea cargar el juego: ");
	String file = scanner.next();

	ObjectInputStream in = null;

	try {
	    // Abrimos los streams iniciador y filtro
	    in = new ObjectInputStream(new FileInputStream(file));
	    // Cargamos los objetos

	} catch (IOException ex) {
	    System.out.println(ex.getMessage());
	} finally {
	    if (in != null) {
		try {
		    in.close();// Cerramos el flujo de entrada
		} catch (IOException ex) {
		    System.out.println(ex.getMessage());
		}
	    }
	}

	return true;
    }
}
