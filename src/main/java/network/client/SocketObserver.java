package network.client;

import org.json.JSONObject;

public interface SocketObserver {
    void onOpen();

    void onMessage(JSONObject s);

    void onClose();

    void onError(Exception e);
}
