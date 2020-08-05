package mc_screen.TCP_Server;

import mc_screen.BlockUtils;
import mc_screen.Main;
import mc_screen.screen.Screen;
import mc_screen.screen.ScreenManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.logging.Logger;

public class PacketHandler {

    public static void handlePacket(int id, int size, byte[] data, Client client, JavaPlugin plugin) throws IOException {
        Logger logger = plugin.getLogger();
        //System.out.println("id: "+id+", size: "+size);
        //System.out.println("data: "+ Arrays.toString(data));

        if (id == 0x01) {
            StringBuilder token = new StringBuilder();
            for (byte value : data) {
                token.append((char) value);
            }

            DataOutputStream output = new DataOutputStream(client.getClient().getOutputStream());

            if (ScreenManager.isValidToken(token.toString())) {
                Screen screen = ScreenManager.getScreen(token.toString());

                if (!screen.getEnable()) {
                    byte[] packet_data = {0x00};
                    output.write(ServerUtil.packet(0x01, packet_data));
                    return;
                }

                if (screen.getUsed()) {
                    for (Client _client : Server.getClients()) {
                        if (_client.getScreen() == screen) {
                            if (!_client.isInterrupted() && !_client.getClient().isClosed() && !_client.getClient().isInputShutdown() && _client.getClient().isConnected()) {
                                byte[] packet_data = {0x00};
                                output.write(ServerUtil.packet(0x01, packet_data));
                                return;
                            }
                        }
                    }
                }

                client.setScreen(screen);
                screen.setUsed(true);

                ByteBuffer buffer = ByteBuffer.allocate(9);

                buffer.put((byte) 0x01);
                buffer.putInt(1, screen.getWidth());
                buffer.putInt(5, screen.getHeight());

                byte[] packet_data = buffer.array();

                output.write(ServerUtil.packet(0x01, packet_data));
            } else {
                byte[] packet_data = {0x00};
                output.write(ServerUtil.packet(0x01, packet_data));
            }
        }
        else if (id == 0x02) {
            int type = data[0];
            data = ServerUtil.sliceByteArray(data, 1);

            ByteBuffer buffer = ByteBuffer.wrap(data);
            int width = buffer.getInt();
            int height = buffer.getInt(4);

            Screen screen = client.getScreen();

            if (!screen.getEnable()) {
                client.setScreen(null);
                return;
            }

            if (width > screen.getWidth() || height > screen.getHeight())
                return;


            if (type == 0x07) {
                short[][] pixels = ServerUtil.decodeUInt8Array2d(data);
                Material[][] blocks = new Material[height][width];

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        blocks[y][x] = BlockUtils.getBlock(pixels[y][x]);
                    }
                }

                ((Main) plugin).frameQueue(screen, blocks);

                /*
                for (Material[] blocks_row : blocks) {
                    String[] names = new String[blocks_row.length];
                    for (int x = 0; x < blocks_row.length; x++) {
                        names[x] = blocks_row[x].name();
                    }

                    logger.info(Arrays.toString(names));
                }
                 */

                //screen.setPixels(blocks, true);
            }

        }
    }

}
