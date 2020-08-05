package mc_screen.screen;


import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.Random;

public class ScreenUtil {

    //public static Material[] tileEntities = {Material.BEEHIVE, Material.BEE_NEST, Material.OAK_SIGN, Material.SPRUCE_SIGN, Material.BIRCH_SIGN, Material.JUNGLE_SIGN, Material.ACACIA_SIGN, Material.DARK_OAK_SIGN, Material.CHEST, Material.TRAPPED_CHEST, Material.DISPENSER, Material.FURNACE, Material.BREWING_STAND, Material.HOPPER, Material.DROPPER, Material.SHULKER_BOX};

    /*
    public static boolean isTileEntity(Material material) {
        return Arrays.asList(tileEntities).contains(material);
    }
    */

    public static String randomString(int length) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < length; i++) {
            string.append((Math.random() > .5) ? (char)(randomInt(97, 123) - 32) : (char)(randomInt(97, 123)));
        }
        return string.toString();
    }

    public static int randomInt(int min, int max) {
        Random rng = new Random();
        return rng.nextInt(max - min) + min;
    }

}
