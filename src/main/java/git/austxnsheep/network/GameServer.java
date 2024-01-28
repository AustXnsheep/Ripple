package git.austxnsheep.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer {
    public static Server server;

    public GameServer() {
        server = new Server();
        registerPackets();
    }

    private void registerPackets() {
        // Register all classes that will be sent over the network.
        // server.getKryo().register(SomeClass.class);
    }

    public void start() throws IOException {
        server.start();
        server.bind(54555, 54777);
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                // Handle received data
            }
        });
    }

    // Method to send data to all connected clients
    public void sendToAll(Object object) {
        server.sendToAllTCP(object);
    }
}
