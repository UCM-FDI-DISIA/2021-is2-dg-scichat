package control.options;

import java.util.Scanner;
import logic.Game;

public class HelpOption extends Option {

    HelpOption(String message) {
	super(message);
    }

    @Override
    public boolean execute(Game game, Scanner scanner) throws ExecuteException {
	String str = "SI NECESITAS AYUDA PARA JUGAR A ESTO ERES UN PRINGAO";
	System.out.println(str);
	System.out.println();
        return false;
    }
}
