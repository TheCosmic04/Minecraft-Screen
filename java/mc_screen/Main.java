package mc_screen;

import mc_screen.TCP_Server.Client;
import mc_screen.TCP_Server.Server;
import mc_screen.commands.ScreenCommand;
import mc_screen.events.BlockEvents_Event;
import mc_screen.events.EntityEvents_Event;
import mc_screen.screen.Screen;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.net.BindException;
import java.util.HashMap;

public class Main extends JavaPlugin {

    private Server server;
    private BukkitTask tickEvent;
    private static HashMap<Screen, Material[][]> frameQueue = new HashMap<Screen, Material[][]>();

    @Override
    public void onEnable() {
        this.getLogger().info("Plugin started!");
        BlockUtils.init();

        new ScreenCommand(this);

        server = new Server(1234, this);
        server.start();


        new BlockEvents_Event(this);
        new EntityEvents_Event(this);

        tickEvent = new BukkitRunnable(){
            @Override
            public void run(){
               for (Screen screen : frameQueue.keySet()) {
                   if (!screen.getEnable()) {
                       frameQueue.remove(screen);
                       return;
                   }
                   Material[][] frame = frameQueue.get(screen);

                   screen.setPixels(frame, true);
                   frameQueue.remove(screen);
               }
            }
        }.runTaskTimer(this, 0L, 1L);
    }

    @Override
    public void onDisable() {
        frameQueue.clear();
        tickEvent.cancel();
        server.interrupt();
        try {
            server.getClients().forEach((client) -> {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            server.getServer().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void frameQueue(Screen screen, Material[][] frame) {
        frameQueue.put(screen, frame);
    }
}
