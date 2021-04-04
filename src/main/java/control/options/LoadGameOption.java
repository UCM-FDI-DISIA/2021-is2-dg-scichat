package control.options;

import java.util.Scanner;
import java.io.*;

import control.options.Option.ExecuteException;
import logic.Game;

public class LoadGameOption extends Option {

    protected LoadGameOption(String title) {
	super(title);
    }

    @Override
    public boolean execute(Game game, Scanner scanner) throws ExecuteException {

	System.out.println("Ingrese el nombre del archivo donde desea guardar el juego: ");
	String file = scanner.next();

	ObjectOutputStream out = null;

	try {
	    // Abrimos los streams iniciador y filtro
	    out = new ObjectOutputStream(new FileOutputStream(file));
	    // Grabamos los objetos

	} catch (IOException ex) {
	    System.out.println(ex.getMessage());
	} finally {
	    if (out != null) {
		try {
		    out.close();// Cerramos el flujo de salida
		} catch (IOException ex) {
		    System.out.println(ex.getMessage());
		}
	    }
	}

	return true;
    }
}
