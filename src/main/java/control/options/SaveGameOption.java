package control.options;

import java.util.Scanner;
import java.io.*;

import exceptions.SaveGameException;
import logic.Game;

public class SaveGameOption extends Option {

    protected SaveGameOption(String title) {
	super(title);
    }

    public boolean execute(Game game, Scanner scanner) throws ExecuteException {

	System.out.println("Ingrese el nombre del archivo donde desea guardar el juego: ");
	String file = scanner.next();

	ObjectOutputStream out = null;
	try {
	    try {
		// Abrimos los streams iniciador y filtro
		out = new ObjectOutputStream(new FileOutputStream(file));
		// Grabamos los objetos

		out.writeObject(game);

	    } catch (IOException ex) {
		System.out.println(ex.getMessage());
		throw new SaveGameException();
	    } finally {
		if (out != null) {
		    try {
			out.close();// Cerramos el flujo de salida
		    } catch (IOException ex) {
			System.out.println(ex.getMessage());
			throw new SaveGameException();
		    }
		}
	    }
	} catch (SaveGameException ex) {
	    throw new ExecuteException(ex.getMessage());
	}
	
	System.out.println("Su archivo se ha guardado con éxito.");

	return true;
    }
}
