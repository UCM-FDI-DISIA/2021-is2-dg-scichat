package graphic;

import control.Controller;
import java.awt.*;
import java.util.HashSet;
import javax.swing.*;
import logic.Cell;
import logic.Game;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.commands.Command;
import network.commands.CommandParser;
import network.commands.PieceMovedCommand;
import network.commands.SurrenderCommand;
import network.models.Room;
import org.json.JSONObject;

public class OnlineGameWindow extends JFrame implements SocketObserver, GameObserver {
    private final Controller ctrl;
    private final SocketClient sc;
    private final Room room;
    private final String roomID;
    private final HashSet<String> localPlayers = new HashSet<>();

    private BoardPanel boardPanel;
    private OptionsPanel optionsPanel;

    private Command pieceMovedCommand = new PieceMovedCommand() {

        @Override
        public void execute(JSONObject data, SocketClient connection) {
            super.execute(data, connection);
            try {
                ctrl.onlineMovePiece(x1, y1, x2, y2, playerID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private Command surrenderCommand = new SurrenderCommand() {

        @Override
        public void execute(JSONObject data, SocketClient connection) {
            super.execute(data, connection);
            ctrl.surrender(getPlayerID());
        }
    };

    private CommandParser commandParser = new CommandParser() {

        @Override
        public Command[] getCommands() {
            return new Command[] { pieceMovedCommand, surrenderCommand };
        }
    };

    OnlineGameWindow(Controller _ctrl, SocketClient _sc, String _roomID, Room _room) {
        super(_sc.getClientID());
        this.sc = _sc;
        this.ctrl = _ctrl;
        this.roomID = _roomID;
        this.room = _room;
    }

    protected void initGUI() {
        try {
            JPanel gameScreen = new JPanel(new BorderLayout());
            boardPanel = new BoardPanel(ctrl);
            optionsPanel = new OptionsPanel(ctrl);
            gameScreen.add(boardPanel, BorderLayout.LINE_START);
            gameScreen.add(optionsPanel, BorderLayout.LINE_END);
            this.setContentPane(gameScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.pack();
    }

    public void display() {
        this.initGUI();
        this.sc.addObserver(this);
        this.ctrl.addObserver(this);
        this.setVisible(true);
    }

    public void addLocalPlayer(String ID) {
        this.localPlayers.add(ID);
    }

    @Override
    public void onMessage(JSONObject s) {
        String type = s.getString("type");
        try {
            Command command = commandParser.parse(type);
            command.execute(s.getJSONObject("data"), this.sc);
        } catch (Exception e) {}
    }

    @Override
    public void onMovedPiece(Cell from, Cell to, String playerID) {
        /// Es un movimiento hecho por un jugador local
        if (localPlayers.contains(playerID)) {
            new PieceMovedCommand(
                from.getRow(),
                from.getCol(),
                to.getRow(),
                to.getCol(),
                this.roomID,
                playerID
            )
            .send(this.sc);
        }
    }

    @Override
    public void onSurrendered(Game game) {
        String playerID = game.getCurrentPlayer().getId();
        if (this.localPlayers.contains(playerID)) {
            new SurrenderCommand(playerID, roomID).send(this.sc);
        }
    }

    @Override
    public void onGameEnded(Game game) {
        System.out.println("Ha ganado el jugador " + game.getWinner().getId());
        JOptionPane.showMessageDialog(
            this,
            "Ha ganado el jugador " + game.getWinner().getId(),
            "Fin",
            JOptionPane.INFORMATION_MESSAGE
        );
    }

    public boolean canMove(String playerID) {
        return this.localPlayers.contains(playerID);
    }

    @Override
    public void onRegister(Game game) {
        String playerID = game.getCurrentPlayer().getId();
        setBlocker(playerID);
    }

    @Override
    public void onEndTurn(Game game) {
        String playerID = game.getCurrentPlayer().getId();
        setBlocker(playerID);
    }

    private void setBlocker(String playerID) {
        if (this.canMove(playerID)) {
            this.setEnabled(true);
            setTitle("Tu turno");
        } else {
            this.setEnabled(false);
            setTitle("Esperando al otro jugador...");
        }
    }
}
