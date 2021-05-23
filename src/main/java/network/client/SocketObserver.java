package network.client;

import org.json.JSONObject;

/**
 * Interfaz de observador para recibir las actualizaciones del Socket
 */
public interface SocketObserver {
    default void onOpen() {}

    default void onMessage(JSONObject s) {}

    default void onClose() {}

    default void onError(Exception e) {}

    default void onClientIDChange(String clientID) {}
}
