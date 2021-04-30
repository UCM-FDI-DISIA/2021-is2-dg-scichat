package network.client;

public interface SocketObserver {
    void onOpen();

    void onMessage(String s);

    void onClose();

    void onError(Exception e);
}
