package mc_screen.events;

import mc_screen.screen.ScreenManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.plugin.java.JavaPlugin;

public class BlockEvents_Event implements Listener {

    private JavaPlugin plugin;

    public BlockEvents_Event(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void handleBlockEvent(BlockEvent event) {
        if (ScreenManager.isScreenBlock(event.getBlock()) && event instanceof Cancellable) {
            ((Cancellable) event).setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        handleBlockEvent((BlockEvent) event);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        handleBlockEvent((BlockEvent) event);
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        handleBlockEvent((BlockEvent) event);
    }

    @EventHandler
    public void onPhysicUpdate(BlockPhysicsEvent event) {
        handleBlockEvent((BlockEvent) event);
    }

    @EventHandler
    public void blockFormEvent(BlockFormEvent event) {
        handleBlockEvent((BlockEvent)event);
    }

    @EventHandler
    public void redstoneEvent(BlockRedstoneEvent event) {
        handleBlockEvent((BlockEvent)event);
    }

    @EventHandler
    public void onBurnBlock(BlockBurnEvent event) {
        handleBlockEvent((BlockEvent)event);
    }
}
