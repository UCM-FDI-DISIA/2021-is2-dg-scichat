package control.options;

import exceptions.InvalidMoveException;
import java.util.Scanner;
import logic.Cell;
import logic.Game;
import logic.gameObjects.Piece;
import logic.gameObjects.Player;

public class MovePieceOption extends Option {

    protected MovePieceOption(String title) {
        super(title);
    }

    @Override
    public boolean execute(Game game, Scanner scanner) throws ExecuteException {
        StringBuilder result = new StringBuilder();
        result.append("Piezas disponibles: \n\n");

        for (Piece piece : game.getCurrentPlayerPieces()) {
            result.append(
                String.format(
                    "   (%s, %s) \n",
                    piece.getPosition().getRow(),
                    piece.getPosition().getCol()
                )
            );
        }
        int row;
        int col;
        Cell selectedCell;
        do {
            /// Imprimir las piezas del jugador actual
            System.out.println(result);

            System.out.println(
                "Introducir las coordenadas de la ficha que quieres mover (-1 -1 para volver al menú): "
            );
            row = scanner.nextInt();
            col = scanner.nextInt();

            if (row == -1 && col == -1) {
                /// Volver al menú de turnos
                return false;
            }

            selectedCell = game.getCell(row, col);
        } while (!game.setSelectedPiece(selectedCell));

        System.out.format("Introduce la nueva posición de la ficha (%d, %d): ", row, col);
        int newRow = scanner.nextInt();
        int newCol = scanner.nextInt();

        Cell newCell = game.getCell(newRow, newCol);

        game.movePiece(newCell);

        return true;
    }
}
