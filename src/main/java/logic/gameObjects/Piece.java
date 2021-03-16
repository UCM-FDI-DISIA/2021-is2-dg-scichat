package logic.gameObjects;

import java.util.Iterator;

import exceptions.InvalidOperationException;
import exceptions.OccupiedCellException;
import exceptions.OutOfBoundsException;
import logic.Cell;

public class Piece {
	private Cell position;
	private Player owner;
	
	public Piece(Cell pos, Player own) throws OccupiedCellException {
		this.owner = own;

		if(!pos.isEmpty()) 
			throw new OccupiedCellException(pos);
		this.position = pos;
		this.position.assign(own.getColor());
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
		this.position.assign(owner.getColor());
	}
	
	public boolean isAtEnd() {
		// TODO: Completar. Tenemos que ver como detectar que estás en el final.
		// Probablemente se tenga que llegar a `Board` para resolverlo
		return true;
	}
}