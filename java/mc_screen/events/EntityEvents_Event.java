package mc_screen.events;

import mc_screen.screen.ScreenManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityEvents_Event implements Listener {

    private JavaPlugin plugin;

    public EntityEvents_Event(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event) {
        if (ScreenManager.isScreenBlock(event.getBlock())) {
            event.setCancelled(true);

            event.getBlock().getState().update(false, false);
        }
    }

}
