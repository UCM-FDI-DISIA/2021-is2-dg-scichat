package graphic;

import control.Controller;
import java.awt.*;
import java.util.HashSet;
import javax.swing.*;
import logic.Cell;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.commands.Command;
import network.commands.CommandParser;
import network.commands.PieceMovedCommand;
import network.models.Room;
import org.json.JSONObject;

public class OnlineGameWindow extends JFrame implements SocketObserver, GameObserver {
    private final Controller ctrl;
    private final SocketClient sc;
    private final Room room;
    private final String roomID;
    private final HashSet<String> localPlayers = new HashSet<>();

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

    private CommandParser commandParser = new CommandParser() {

        @Override
        public Command[] getCommands() {
            return new Command[] { pieceMovedCommand };
        }
    };

    OnlineGameWindow(Controller _ctrl, SocketClient _sc, String _roomID, Room _room) {
        super(_sc.getClientID());
        this.sc = _sc;
        this.sc.addObserver(this);
        this.ctrl = _ctrl;
        this.ctrl.addObserver(this);

        this.roomID = _roomID;
        this.room = _room;

        this.initGUI();
    }

    protected void initGUI() {
        try {
            JPanel gameScreen = new JPanel(new BorderLayout());
            gameScreen.add(new BoardPanel(ctrl), BorderLayout.LINE_START);
            gameScreen.add(new OptionsPanel(ctrl), BorderLayout.LINE_END);
            this.setContentPane(gameScreen);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.pack();
    }

    public void display() {
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
}
