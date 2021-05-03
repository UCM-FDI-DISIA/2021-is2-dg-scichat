/**
 * package network.models;
 * <p>
 * import org.json.JSONArray;
 * import org.json.JSONObject;
 * import utils.Mode;
 * <p>
 * import java.util.LinkedList;
 * import java.util.List;
 * import java.util.Queue;
 * <p>
 * public class ServerRoomConfig extends RoomConfig {
 * /// Lista de configuraciones disponibles para elegir
 * /// Util para el servidor
 * protected LinkedList<PlayerConfig> playerConfigs;
 * <p>
 * public ServerRoomConfig(JSONObject data) {
 * super(data);
 * <p>
 * JSONArray playerConfigArray = data.getJSONArray("players");
 * this.playerConfigs = new LinkedList<>();
 * <p>
 * for (Object p : playerConfigArray) {
 * playerConfigs.add(new PlayerConfig((JSONObject) p));
 * }
 * <p>
 * this.numPlayers = this.playerConfigs.size();
 * }
 * <p>
 * public Queue<PlayerConfig> getPlayerConfigs() {
 * return playerConfigs;
 * }
 * }
 */
