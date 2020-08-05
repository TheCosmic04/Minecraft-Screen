package mc_screen.TCP_Server;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Logger;

public class Server extends Thread {

    private ServerSocket server;
    private JavaPlugin plugin;
    private Logger logger;
    private int curr_id = 0;
    private static ArrayList<Client> clients = new ArrayList<Client>();

    public Server(int port, JavaPlugin plugin) {
        try {
            this.server = new ServerSocket(port);
            this.plugin = plugin;
            this.logger = plugin.getLogger();
        } catch (IOException err) {
            err.printStackTrace();
            plugin.onDisable();
        }
    }

    public void run() {
        logger.info("Server started.");
        while (!this.isInterrupted()) {
            Client client = null;
            try {
                Socket connection = server.accept();
                connection.setKeepAlive(true);
                 client = new Client(connection, curr_id++, plugin);

                logger.info("Client id:"+client.getClientId()+" connected.");
                client.start();

                clients.add(client);
                //client.close();
            } catch (IOException err) {
                //err.printStackTrace();
            }
        }
    }

    public ServerSocket getServer() {
        return server;
    }

    public static ArrayList<Client> getClients() {
        return clients;
    }
}
