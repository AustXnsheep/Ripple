package git.austxnsheep.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.IOException;

public class GameClient {
    private Client client;

    public GameClient() {
        client = new Client();
        registerPackets();
        client.start();
    }

    private void registerPackets() {
        // Register the same classes as the server
        // client.getKryo().register(SomeClass.class);
    }

    public void connect(String address) throws IOException {
        client.connect(5000, address, 54555, 54777);
        client.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                // Handle received data
            }
        });
    }

    // Method to send data to the server
    public void sendToServer(Object object) {
        client.sendTCP(object);
    }
}
