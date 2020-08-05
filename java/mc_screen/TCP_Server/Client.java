package mc_screen.TCP_Server;

import mc_screen.screen.Screen;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class Client extends Thread {

    private Socket client;
    private JavaPlugin plugin;
    private Logger logger;
    private int id;
    private Screen screen;
    private boolean read_packets = true;

    public Client(Socket client, int id, JavaPlugin plugin) {
        this.client = client;
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        this.id = id;
    }

    public void run() {
        try {
            DataInputStream stream = new DataInputStream(client.getInputStream());
            parsePacket(stream);
        } catch (IOException err) {
            //err.printStackTrace();
            try {
                client.close();
                this.interrupt();
            } catch (IOException e) {
                logger.info("Client id:" + id + " disconnected!");
                if (screen != null) {
                    screen.setUsed(false);
                }
               // e.printStackTrace();
            }
        }
    }

    public void parsePacket(DataInputStream stream) throws IOException {
        try {
            if (!read_packets || client.isClosed() || client.isInputShutdown())
                return;
            byte[] header_bytes = new byte[5];
            int header_byte_count = stream.read(header_bytes, 0, header_bytes.length);

            if (header_byte_count < 5) {
                logger.info("Client id:"+id+" sent an invalid packet! (invalid header bytes)");
                parsePacket(stream);
                return;
            }

            ByteBuffer buffer = ByteBuffer.wrap(ServerUtil.sliceByteArray(header_bytes, 1));

            int id = header_bytes[0];
            int size = buffer.getInt();

            byte[] data_bytes = new byte[size];
            int data_byte_count = stream.read(data_bytes);

            if (data_byte_count < size){
                logger.info("Client id:"+id+" sent an invalid packet! (invalid size)");
                parsePacket(stream);
                return;
            }
            PacketHandler.handlePacket(id, size, data_bytes, this, plugin);

            parsePacket(stream);

        } catch (IOException err) {
            //err.printStackTrace();
            read_packets = true;
            try {
                client.close();
                if (screen != null) {
                    screen.setUsed(false);
                }
                this.interrupt();
            } catch (IOException e) {
                //e.printStackTrace();
            }
            logger.info("Client id:" + id + " disconnected!");
        }
    }

    public void close() throws IOException {
        read_packets = false;
        if (!client.isClosed())
            client.close();
        if (screen != null) {
            screen.setUsed(false);
        }
        this.interrupt();
    }

    public Screen getScreen() { return screen; }
    public void setScreen(Screen screen) { this.screen = screen; }

    public Socket getClient() { return client; }
    public int getClientId() { return id; }
}
