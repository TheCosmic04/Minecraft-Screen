package mc_screen.screen;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Iterator;

public class ScreenManager {

    public static final int MAX_AREA = 254*254;
    private static int curr_id = 0;
    private static final int TOKEN_LENGTH = 6;
    private static HashMap<String, Screen> screenList = new HashMap<String, Screen>();

    public static Screen createScreen(Location location, int width, int height, JavaPlugin plugin) {
        if (!isValidSize(width, height))
            return null;
        String token = ScreenUtil.randomString(TOKEN_LENGTH);
        Screen screen = new Screen(curr_id++, token, width, height, location, plugin);

        screenList.put(token, screen);
        return screen;
    }

    public static Screen[] getScreens() {
        return screenList.values().toArray(new Screen[0]);
    }

    public static String[] getTokens() {
        return screenList.keySet().toArray(new String[0]);
    }

    public static boolean isValidToken(String token) {
       return screenList.containsKey(token);
    }
    public static boolean isValidSize(int width, int height) {
        return width * height < MAX_AREA;
    }

    public static boolean isScreenBlock(Block block) {
        for (Screen screen : screenList.values()) {
            if (screen.isInScreen(block.getLocation()))
                return true;
        }
        return false;
    }

    public static boolean deleteScreen(String token) {
        if (!screenList.containsKey(token))
            return false;

        screenList.get(token).setEnable(false);
        screenList.remove(token);
        return true;
    }

    public static boolean deleteScreen(int id) {
        Screen screen = getScreen(id);
        if (screen == null)
            return false;

        String token = screen.getToken();

        screenList.get(token).setEnable(false);
        screenList.remove(token);
        return true;
    }

    public static Screen getScreen(String token) {
        return screenList.getOrDefault(token, null);
    }

    public static Screen getScreen(int id) {
        if (id > curr_id)
            return null;
        for (Screen screen : screenList.values()) {
            if (screen.getId() == id)
                return screen;
        }
        return null;
    }

}
