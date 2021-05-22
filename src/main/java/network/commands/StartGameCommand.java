package network.commands;

import org.json.JSONObject;

/**
 * Comando para empezar una partida online
 * <p>
 * Por el diseño de modelo Servidor-Cliente, no habrá que hacer nada en el servidor,
 * simplemente se reenvía esta petición al resto de los jugadores, para que actualicen la interfaz
 */
public class StartGameCommand extends Command {
    private String roomID;

    public StartGameCommand() {
        super("START_GAME");
    }

    public StartGameCommand(String _roomID) {
        this();
        this.roomID = _roomID;
    }

    @Override
    public JSONObject getData() {
        return new JSONObject().put("roomID", this.roomID);
    }
}
