package network.client;

import org.json.JSONObject;

public interface SocketObserver {
    default void onOpen() {}

    default void onMessage(JSONObject s) {}

    default void onClose() {}

    default void onError(Exception e) {}

    default void onClientIDChange(String clientID) {}
}
