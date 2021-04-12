package utils;

import java.awt.Color;

public class Util {
    public static String mode2str(Mode playMode) {
        switch (playMode) {
            case Fast:
                return "Modo rápido";
            case Traditional:
                return "Juego Tradicional";
            default:
                return "TO BE IMPLEMENTED IN Util.mode2str";
        }
    }
}
