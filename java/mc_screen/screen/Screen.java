package mc_screen.screen;

import mc_screen.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class Screen {


    private int x, y, z, width, height, id;
    private JavaPlugin plugin;
    private World world;
    private Material[][] pixels;
    private final Material[][] DEFAULT_PIXELS;
    private boolean enable = true, used = false;
    private String token;

    public Screen(int id, String token, int width, int height, Location location, JavaPlugin plugin) {
        this.id = id;
        this.token = token;
        this.width = width;
        this.height = height;
        this.x = (int)location.getX();
        this.y = (int)location.getY();
        this.z = (int)location.getZ();
        this.world = location.getWorld();
        this.plugin = plugin;

        DEFAULT_PIXELS = new Material[height][width];
        for (int i = 0; i < height; i++) {
            Material[] pixel_row = new Material[width];
            Arrays.fill(pixel_row, Material.BLACK_CONCRETE);
            DEFAULT_PIXELS[i] = pixel_row;
        }
        pixels = DEFAULT_PIXELS;

        //int min_x = this.x - width/2 - 1;
        //int max_x = this.x + width/2;
        //int min_z = this.z - height/2 - 1;
        //int max_z = this.z + height/2;
        //plugin.getLogger().info("min_x: " + min_x + ", max_x: " + max_x + ", min_z: " + min_z + ", max_z: " + max_z);

        update(DEFAULT_PIXELS);
    }

    public void update() {
        if (!enable)
            return;
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                Location location = getPixelLocation(x, y);
                location.setX(location.getX() - (double)(pixels[0].length/2));
                location.setZ(location.getZ() - (double)(pixels.length/2));

                if (location.getBlock().getType() != pixels[y][x])
                    location.getBlock().setType(pixels[y][x], false);

            }
        }
    }

    public void update(Material[][] pixels) {
        if (!enable)
            return;
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[y].length; x++) {
                Location location = getPixelLocation(x, y);
                location.setX(location.getX() - (double)(pixels[0].length/2));
                location.setZ(location.getZ() - (double)(pixels.length/2));

                if (location.getBlock().getType() != pixels[y][x])
                    location.getBlock().setType(pixels[y][x], false);
            }
        }
    }

    public Location getPixelLocation(int x, int y) {
        Location location = new Location(world, x, y, z);
        return new Location(world, this.x + x, this.y, this.z + y);
    }

    public Material getPixelMaterial(int x, int y) {
        x = Math.min(x, width-1);
        y = Math.min(y, height-1);
        return pixels[y][x];
    }

    public void setPixel(int x, int y, Material material) {
        if (!enable)
            return;
        x = Math.min(x, width-1);
        y = Math.min(y, height-1);
        pixels[y][x] = material;
    }

    public void setPixel(int x, int y, Material material, boolean update) {
        if (!enable)
            return;
        x = Math.min(x, width-1);
        y = Math.min(y, height-1);
        pixels[y][x] = material;

        if (update)
            this.update();
    }

    public void setPixels(Material[][] materials) {
        if (!enable)
            return;
        if (materials.length > height || materials[0].length > width)
            return;

        pixels = materials;
    }

    public void setPixels(Material[][] materials, boolean update) {
        if (!enable)
            return;
        if (materials.length > height || materials[0].length > width)
            return;

        pixels = materials;
        if (update)
            this.update();
    }

    public boolean isInScreen(Location location) {
        if (!enable)
            return false;
        if (location.getY() != this.y)
            return false;
        int x = (int) location.getX();
        int z = (int) location.getZ();

        int min_x = this.x - width/2 - 1;
        int max_x = this.x + width/2;
        int min_z = this.z - height/2 - 1;
        int max_z = this.z + height/2 + 1;

        //plugin.getLogger().info("x: " + x + ", z: " + z);
        //plugin.getLogger().info("min_x: " + min_x + ", max_x: " + max_x + ", min_z: " + min_z + ", max_z: " + max_z);

        //plugin.getLogger().info("Block pos: x:" + x + ", z: "+ y);

        //plugin.getLogger().info("x1: "+ (x > this.x - width / 2) + ", x2: "+(x < this.x + width / 2 - 1) + ", y1: "+ (y > this.y - height / 2) + ", y2: "+ (y < this.y + height / 2));
        //plugin.getLogger().info("x1: "+ (this.x - width / 2) + ", x2: "+(this.x + width / 2) + ", y1: "+ (this.y - height / 2) + ", y2: "+ (this.y + height / 2));

        return x > min_x && x < max_x && z > min_z && z < max_z;
    }

    public boolean getEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public boolean getUsed() {
        return used;
    }

    public Material[][] getPixels() {
        return pixels;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
