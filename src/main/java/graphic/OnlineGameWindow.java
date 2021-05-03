package graphic;

import control.Controller;
import java.awt.*;
import javax.swing.*;
import logic.Cell;
import network.client.SocketClient;
import network.client.SocketObserver;
import network.commands.Command;
import network.commands.CommandParser;
import network.commands.PieceMovedCommand;
import network.models.Room;
import network.models.SocketMessage;
import org.json.JSONArray;
import org.json.JSONObject;

public class OnlineGameWindow extends JFrame implements SocketObserver, GameObserver {
    private final Controller ctrl;
    private final SocketClient sc;
    private final Room room;
    private final String roomID;

    private Command pieceMovedCommand = new PieceMovedCommand() {

        @Override
        public SocketMessage execute(JSONObject _data, SocketClient connection) {
            JSONArray fromArray = _data.getJSONArray("from");
            JSONArray toArray = _data.getJSONArray("to");

            int x1 = fromArray.getInt(0);
            int y1 = fromArray.getInt(1);
            int x2 = toArray.getInt(0);
            int y2 = toArray.getInt(1);

            try {
                ctrl.movePiece(x1, y1, x2, y2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    };

    private CommandParser commandParser = new CommandParser() {

        @Override
        public Command[] getCommands() {
            return new Command[] { pieceMovedCommand };
        }
    };

    OnlineGameWindow(Controller _ctrl, SocketClient _sc, String _roomID, Room _room) {
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
        this.setVisible(true);
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
    public void onMovedPiece(Cell from, Cell piece) {
        new PieceMovedCommand(from, piece, this.roomID).send(this.sc);
    }
}
