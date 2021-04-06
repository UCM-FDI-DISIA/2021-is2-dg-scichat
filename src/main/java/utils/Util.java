package utils;

import java.awt.Color;

public class Util {
    public static String col2str(Color color) {
	if(color == Color.BLACK) return "BLACK";
	if(color == Color.WHITE) return "WHITE";
	if(color == Color.RED) return "RED";
	if(color == Color.GREEN) return "GREEN";
	if(color == Color.BLUE) return "BLUE";
	if(color == Color.MAGENTA) return "MAGENTA";
	if(color == Color.YELLOW) return "YELLOW";
	if(color == Color.CYAN) return "CYAN";
	if(color == Color.ORANGE) return "ORANGE";
	return "TO BE IMPLEMENTED IN Util.col2str";
    }
    
    public static String mode2str(Mode playMode) {
	switch(playMode) {
	case Fast:
	    return "Fast Mode";
	case Traditional:
	    return "Traditional Mode";
	default:
	    return "TO BE IMPLEMENTED IN Util.mode2str";
	}
    }
}
