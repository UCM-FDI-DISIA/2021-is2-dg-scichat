package logic.gameObjects;

import logic.Board;
import logic.Board.Color;

public class Player{
	
	private Board.Color color;
	private Piece[] pieces=new Piece[10];
	
	public Player(){
		color = Color.Blue;
	}	
	
	public Player(Board.Color color){
		this.color = color;
	}
	
	
	public Board.Color getColor() {
		return color; 
	}
}