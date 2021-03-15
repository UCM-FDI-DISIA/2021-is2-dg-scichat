package logic.gameObjects;

import logic.Board;
import logic.Board.Color;

public class Player{
	private Board.Color color;
	public Player(){
		color = Color.Blue;
	}
	
	public Board.Color getColor() {
		return color; 
	}
}