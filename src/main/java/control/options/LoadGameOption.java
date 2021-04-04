package control.options;

import java.util.Scanner;
import java.io.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

import control.options.Option.ExecuteException;
import exceptions.LoadGameException;
import logic.Game;

public class LoadGameOption extends Option {

    protected LoadGameOption(String title) {
	super(title);
    }

    @Override
    public boolean execute(Game game, Scanner scanner) throws ExecuteException {

	System.out.println("Ingrese el nombre del archivo desde donde desea cargar el juego: ");
	String file = scanner.next();

	ObjectInputStream in = null;
	try {
	    try {
		// Abrimos los streams iniciador y filtro
		in = new ObjectInputStream(new FileInputStream(file));
		// Cargamos los objetos

		game = (Game) in.readObject();

	    } catch (IOException ex) {
		System.out.println(ex.getMessage());
		throw new LoadGameException();
	    } catch (ClassNotFoundException ex) {
		System.out.println(ex.getMessage());
		throw new LoadGameException();
	    } finally {
		if (in != null) {
		    try {
			in.close();// Cerramos el flujo de entrada
		    } catch (IOException ex) {
			System.out.println(ex.getMessage());
			throw new LoadGameException();
		    }
		}
	    }
	} catch (LoadGameException ex) {
	    throw new ExecuteException(ex.getMessage());
	}

	return true;
    }
}
