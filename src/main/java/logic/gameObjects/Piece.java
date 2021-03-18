package logic.gameObjects;

import java.util.Iterator;

import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import exceptions.OutOfBoundsException;
import logic.Board;
import logic.Cell;

public class Piece {
	
	//El color es lo que hace que una ficha pertenezca a un jugador
	private Board.Color color;
	private Cell position;
	//Si dan un Piece a un Player el Player quiere saber cual es de entre todas las que tiene
	private int id;
	
	public Board.Color getColor() {
		return color;
	}

	public void setColor(Board.Color color) {
		this.color = color;
	}

	public int getId() {
		return id;
	}

	public Piece(Cell pos, Board.Color color) throws OccupiedCellException {
		this.color=color;
		//Asumimos id=-1 significa id no definido, para debug
		this.id=-1;
		if(!pos.isEmpty()) 
			throw new OccupiedCellException(pos);
		this.position = pos;
		this.position.assign(this.color);
	}
	
	public Piece(Cell pos, Board.Color color, int id) throws OccupiedCellException {
		this.color=color;
		this.id=id;
		if(!pos.isEmpty()) 
			throw new OccupiedCellException(pos);
		this.position = pos;
		this.position.assign(this.color);
	}
	
	public Cell getPosition() {
		return this.position;
	}
	
	public void tryToMoveTo(Cell targetPosition) throws InvalidOperationException, OutOfBoundsException {
		// Comprobamos que nos podemos mover hacia la posición dada
		// Para ello, tiene que haber una (Y solo una) ficha en la 
		// posición intermedia, y la posición dada tiene que estar
		// en la misma diagonal que la posición actual
		
		if(targetPosition.isEmpty()) {
			if(!position.getNeighbours().contains(targetPosition)) {
				Iterator<Cell> it = this.position.getIteratorTowards(targetPosition);
				Cell mid = this.position.getMiddleCellTowards(targetPosition);
				
				while(it.hasNext()) {
					Cell curr = it.next();
					if(curr != mid && !curr.isEmpty())
						throw new InvalidOperationException("There are cells obstructing the way.");
					if(curr == mid && curr.isEmpty()) {
						throw new InvalidOperationException("There is no cell in the middle.");
					}
				}
			}
			// else → Queremos mover la pieza a uno de los adyacentes, que está vacio. Se puede
		}
		else throw new InvalidOperationException("The target cell needs to be empty.");
	}
	
	public void move(Cell targetPosition) throws InvalidOperationException, OutOfBoundsException {
		tryToMoveTo(targetPosition);
		this.position.remove();
		this.position = targetPosition;
		this.position.assign(this.color);
	}
	
	//Tal vez se le pueda pasar como argumento cual es End con la clase Board.Side
	//La verdad es que me vendria muy bien
	//-Antimateria
	public boolean isAtEnd() {
		// TODO: Completar. Tenemos que ver como detectar que estás en el final.
		// Probablemente se tenga que llegar a `Board` para resolverlo
		return true;
	}
}