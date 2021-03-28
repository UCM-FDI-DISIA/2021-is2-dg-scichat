package control.options;

import exceptions.InvalidMoveException;
import logic.Cell;
import logic.Game;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;

import java.util.Scanner;

public class MovePieceOption extends Option {
    protected MovePieceOption(String title) {
        super(title);
    }

    @Override
    public boolean execute(Game game, Scanner scanner) throws ExecuteException {
        Player currentPlayer = game.getCurrentPlayer();

        System.out.println(currentPlayer.piecesToString());

        System.out.println("Introducir las coordenadas de la ficha que quieres mover (-1 -1 para volver al menú): ");

        int row = scanner.nextInt();
        int col = scanner.nextInt();

        if (row == -1 && col == -1) {
            /// Volver al menú de turnos
            return false;
        }

        Cell selectedCell = game.getCell(row, col);
        if (selectedCell == null) {
            throw new ExecuteException(String.format("No existe una celda en posición (%d, %d) \n", row, col));
        }

        Piece selectedPiece = selectedCell.getPiece();
        if (selectedPiece == null) {
            throw new ExecuteException(String.format("No existe una pieza en posición (%d, %d) \n", row, col));
        }

        if (!currentPlayer.hasPiece(selectedPiece)) {
            throw new ExecuteException(String.format("El jugador actual no tiene una pieza en posición (%d, %d) \n", row, col));
        }

        System.out.format("Introduce la nueva posición de la ficha (%d, %d): ", row, col);
        int newRow = scanner.nextInt();
        int newCol = scanner.nextInt();

        Cell newCell = game.getCell(newRow, newCol);
        if (newCell == null) {
            throw new ExecuteException(String.format("No existe una celda en posición (%d, %d) \n", newRow, newCol));
        }

        if (!newCell.isEmpty()) {
            throw new ExecuteException(String.format("La celda (%d, %d) está ocupada \n", newRow, newCol));
        }

        try {
            selectedPiece.move(newCell);
        } catch (InvalidMoveException e) {
            throw new ExecuteException(String.format("[ERROR]: Movimiento inválida a posición (%d, %d) \n", newRow, newCol));
        }

        return true;
    }
}
