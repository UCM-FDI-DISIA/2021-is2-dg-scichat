package control;

import logic.*;
import logic.Board.Color;

public class Main {
    public static void main(String[] args){
        Board b = new Board();
        try {
        	Cell c = new Cell(6,6,b);
        	
        	c.assign(Color.Red);
        	
        	for(Cell n : c.getNeighbours()) {
        		n.assign(Color.Yellow);
        	}
        	
        	c.getLowerLeft().remove();
        	
        	System.out.println(b);
        }
        catch(Exception e) {
        	
        }
    }
}