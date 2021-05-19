package network.client;

import java.net.URI;

public class SocketThread extends Thread {
    private SocketClient connection;
    private URI serverURI;

    public SocketThread(URI _serverURI) {
        this.serverURI = _serverURI;

        this.connection = new SocketClient(this.serverURI);
    }

    public void addObserver(SocketObserver o) {
        this.connection.addObserver(o);
    }

    public void removeObserver(SocketObserver o) {
        this.connection.removeObserver(o);
    }

    public void close() {
        this.connection.close();
    }

    public SocketClient getConnection() {
        return this.connection;
    }

    @Override
    public void run() {
        this.connection.connect();
    }
}
