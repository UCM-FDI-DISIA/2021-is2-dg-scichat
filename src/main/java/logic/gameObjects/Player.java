package logic.gameObjects;

import java.util.HashSet;

import logic.Board;
import logic.Board.Color;
import logic.Board.Side;

public class Player{
	
	private Board.Color color; // Color asignado al jugador
	private HashSet<Piece> pieces=new HashSet<Piece>(); // Fichas del jugador
	private Piece selectedPiece=null;
	
	public Player(){
		color = Color.Blue;
	}

	public Player(Board.Color color) {
		this.color = color;
	}

	public Board.Color getColor() {
		return color;
	}

	/**
	 * Pone piece como pieza seleccionada por el jugador con la que realizara varias
	 * de sus acciones
	 * 
	 * @param piece Piece que el jugador quiere seleccionar
	 * @return true si es suya y puede seleccionarla, false si no
	 */
	public boolean selectPiece(Piece piece) {

		if(!pieces.contains(piece))
			return false;
		this.selectedPiece=piece;
		return true;
	};

	/**
	 * Deselecciona la ficha seleccionada si tiene alguna fica seleccionada, si no
	 * no hace nada
	 */
	public void deselectPiece() {
		this.selectedPiece=null;
	}

	/**
	 * Devuelve si el jugador tiene una pieza seleccionada
	 * 
	 * @return true si el jugador tiene una pieza seleccionada, false si no
	 */
	public boolean hasSelectedPiece() {
		return this.selectedPiece!=null;
	}

	/**
	 * 
	 * @return true si todas las fichas del jugador se encuentran en posiciones
	 *         ganadoras
	 */
	public boolean isAWinner() {
		for (Piece pc : pieces)
			if (!side.getOpposeCells().contains(pc.getPosition())) {
				return false;
			}
		return true;
	}

}