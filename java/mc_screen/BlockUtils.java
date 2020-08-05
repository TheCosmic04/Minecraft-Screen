package mc_screen;

import com.mysql.fabric.xmlrpc.base.Array;
import org.bukkit.Material;

import java.util.*;

public class BlockUtils {

    private static final HashMap<Integer, Material> blockIds = new  HashMap<Integer, Material>();
    private static final ArrayList<String> blockNames = new ArrayList<String>();
    private static boolean iniliazied = false;

    public static void init() {
        blockIds.put(0, Material.ACACIA_LOG);
        blockIds.put(1, Material.ACACIA_PLANKS);
        blockIds.put(2, Material.ANDESITE);
        blockIds.put(3, Material.BIRCH_LOG);
        blockIds.put(4, Material.BEDROCK);
        blockIds.put(5, Material.BIRCH_PLANKS);
        blockIds.put(6, Material.BLACK_CONCRETE);
        blockIds.put(7, Material.BLACK_CONCRETE_POWDER);
        blockIds.put(8, Material.BLACK_TERRACOTTA);
        blockIds.put(9, Material.BLACK_WOOL);
        blockIds.put(10, Material.BLUE_CONCRETE);
        blockIds.put(11, Material.BLUE_CONCRETE_POWDER);
        blockIds.put(12, Material.BLUE_ICE);
        blockIds.put(13, Material.BLUE_TERRACOTTA);
        blockIds.put(14, Material.BLUE_WOOL);
        blockIds.put(15, Material.BONE_BLOCK);
        blockIds.put(16, Material.BOOKSHELF);
        blockIds.put(17, Material.BRICKS);
        blockIds.put(18, Material.BROWN_CONCRETE);
        blockIds.put(19, Material.BROWN_CONCRETE_POWDER);
        blockIds.put(20, Material.BROWN_MUSHROOM_BLOCK);
        blockIds.put(21, Material.BROWN_TERRACOTTA);
        blockIds.put(22, Material.BROWN_WOOL);
        blockIds.put(23, Material.CARTOGRAPHY_TABLE);
        blockIds.put(24, Material.CHISELED_QUARTZ_BLOCK);
        blockIds.put(25, Material.CHISELED_RED_SANDSTONE);
        blockIds.put(26, Material.CHISELED_SANDSTONE);
        blockIds.put(27, Material.CARVED_PUMPKIN);
        blockIds.put(28, Material.CHISELED_STONE_BRICKS);
        blockIds.put(29, Material.COAL_BLOCK);
        blockIds.put(30, Material.COAL_ORE);
        blockIds.put(31, Material.COARSE_DIRT);
        blockIds.put(32, Material.COBBLESTONE);
        blockIds.put(33, Material.CRACKED_STONE_BRICKS);
        blockIds.put(34, Material.CRAFTING_TABLE);
        blockIds.put(35, Material.CUT_RED_SANDSTONE);
        blockIds.put(36, Material.CUT_SANDSTONE);
        blockIds.put(37, Material.CYAN_CONCRETE);
        blockIds.put(38, Material.CYAN_CONCRETE_POWDER);
        blockIds.put(39, Material.CYAN_TERRACOTTA);
        blockIds.put(40, Material.CYAN_WOOL);
        blockIds.put(41, Material.DARK_OAK_LOG);
        blockIds.put(42, Material.DARK_PRISMARINE);
        blockIds.put(43, Material.DIAMOND_BLOCK);
        blockIds.put(44, Material.DIAMOND_ORE);
        blockIds.put(45, Material.DIORITE);
        blockIds.put(46, Material.DIRT);
        blockIds.put(47, Material.EMERALD_BLOCK);
        blockIds.put(48, Material.EMERALD_ORE);
        blockIds.put(49, Material.END_STONE);
        blockIds.put(50, Material.END_STONE_BRICKS);
        blockIds.put(51, Material.FLETCHING_TABLE);
        blockIds.put(52, Material.GLOWSTONE);
        blockIds.put(53, Material.GOLD_BLOCK);
        blockIds.put(54, Material.GOLD_ORE);
        blockIds.put(55, Material.GRANITE);
        blockIds.put(56, Material.GRASS_PATH);
        blockIds.put(57, Material.GRAVEL);
        blockIds.put(58, Material.GRAY_CONCRETE);
        blockIds.put(59, Material.GRAY_TERRACOTTA);
        blockIds.put(60, Material.GRAY_CONCRETE_POWDER);
        blockIds.put(61, Material.GRAY_WOOL);
        blockIds.put(62, Material.GREEN_CONCRETE);
        blockIds.put(63, Material.GREEN_CONCRETE_POWDER);
        blockIds.put(64, Material.GREEN_TERRACOTTA);
        blockIds.put(65, Material.GREEN_WOOL);
        blockIds.put(66, Material.HAY_BLOCK);
        blockIds.put(67, Material.HONEYCOMB_BLOCK);
        blockIds.put(68, Material.HONEY_BLOCK);
        blockIds.put(69, Material.ICE);
        blockIds.put(70, Material.IRON_BLOCK);
        blockIds.put(71, Material.IRON_ORE);
        blockIds.put(72, Material.JUNGLE_LOG);
        blockIds.put(73, Material.JUNGLE_PLANKS);
        blockIds.put(74, Material.JACK_O_LANTERN);
        blockIds.put(75, Material.LAPIS_BLOCK);
        blockIds.put(76, Material.LAPIS_ORE);
        blockIds.put(77, Material.LIGHT_BLUE_CONCRETE);
        blockIds.put(78, Material.LIGHT_BLUE_CONCRETE_POWDER);
        blockIds.put(79, Material.LIGHT_BLUE_TERRACOTTA);
        blockIds.put(80, Material.LIGHT_BLUE_WOOL);
        blockIds.put(81, Material.LIGHT_GRAY_CONCRETE);
        blockIds.put(82, Material.LIGHT_GRAY_CONCRETE_POWDER);
        blockIds.put(83, Material.LIGHT_GRAY_WOOL);
        blockIds.put(84, Material.LIGHT_GRAY_TERRACOTTA);
        blockIds.put(85, Material.LIME_CONCRETE_POWDER);
        blockIds.put(86, Material.LIME_CONCRETE);
        blockIds.put(87, Material.LIME_TERRACOTTA);
        blockIds.put(88, Material.LIME_WOOL);
        blockIds.put(89, Material.LOOM);
        blockIds.put(90, Material.MAGENTA_CONCRETE);
        blockIds.put(91, Material.MAGENTA_CONCRETE_POWDER);
        blockIds.put(92, Material.MAGENTA_TERRACOTTA);
        blockIds.put(93, Material.MAGENTA_WOOL);
        blockIds.put(94, Material.MELON);
        blockIds.put(95, Material.MOSSY_COBBLESTONE);
        blockIds.put(96, Material.MOSSY_STONE_BRICKS);
        blockIds.put(97, Material.MUSHROOM_STEM);
        blockIds.put(98, Material.MYCELIUM);
        blockIds.put(99, Material.NETHERRACK);
        blockIds.put(100, Material.NETHER_BRICKS);
        blockIds.put(101, Material.NETHER_QUARTZ_ORE);
        blockIds.put(102, Material.NETHER_WART_BLOCK);
        blockIds.put(103, Material.OAK_LOG);
        blockIds.put(104, Material.OAK_PLANKS);
        blockIds.put(105, Material.OBSERVER);
        blockIds.put(106, Material.OBSIDIAN);
        blockIds.put(107, Material.ORANGE_CONCRETE);
        blockIds.put(108, Material.ORANGE_TERRACOTTA);
        blockIds.put(109, Material.ORANGE_CONCRETE_POWDER);
        blockIds.put(110, Material.ORANGE_WOOL);
        blockIds.put(111, Material.PACKED_ICE);
        blockIds.put(112, Material.PINK_CONCRETE);
        blockIds.put(113, Material.PINK_CONCRETE_POWDER);
        blockIds.put(114, Material.PINK_TERRACOTTA);
        blockIds.put(115, Material.PINK_WOOL);
        blockIds.put(116, Material.PODZOL);
        blockIds.put(117, Material.POLISHED_ANDESITE);
        blockIds.put(118, Material.POLISHED_DIORITE);
        blockIds.put(119, Material.POLISHED_GRANITE);
        blockIds.put(120, Material.PRISMARINE_BRICKS);
        blockIds.put(121, Material.PUMPKIN);
        blockIds.put(122, Material.PURPLE_CONCRETE);
        blockIds.put(123, Material.PURPLE_CONCRETE_POWDER);
        blockIds.put(124, Material.PURPLE_WOOL);
        blockIds.put(125, Material.PURPLE_TERRACOTTA);
        blockIds.put(126, Material.PURPUR_BLOCK);
        blockIds.put(127, Material.PURPUR_PILLAR);
        blockIds.put(128, Material.QUARTZ_BLOCK);
        blockIds.put(129, Material.QUARTZ_PILLAR);
        blockIds.put(130, Material.REDSTONE_BLOCK);
        blockIds.put(131, Material.REDSTONE_LAMP);
        blockIds.put(132, Material.REDSTONE_ORE);
        blockIds.put(133, Material.RED_CONCRETE);
        blockIds.put(134, Material.RED_CONCRETE_POWDER);
        blockIds.put(135, Material.RED_MUSHROOM_BLOCK);
        blockIds.put(136, Material.RED_NETHER_BRICKS);
        blockIds.put(137, Material.RED_SAND);
        blockIds.put(138, Material.RED_SANDSTONE);
        blockIds.put(139, Material.RED_TERRACOTTA);
        blockIds.put(140, Material.RED_WOOL);
        blockIds.put(141, Material.SAND);
        blockIds.put(142, Material.SANDSTONE);
        blockIds.put(143, Material.SCAFFOLDING);
        blockIds.put(144, Material.SLIME_BLOCK);
        blockIds.put(145, Material.SMITHING_TABLE);
        blockIds.put(146, Material.SNOW_BLOCK);
        blockIds.put(147, Material.SOUL_SAND);
        blockIds.put(148, Material.SPONGE);
        blockIds.put(149, Material.SPRUCE_LOG);
        blockIds.put(150, Material.SPRUCE_PLANKS);
        blockIds.put(151, Material.STONE);
        blockIds.put(152, Material.STONECUTTER);
        blockIds.put(153, Material.STONE_BRICKS);
        blockIds.put(154, Material.STRIPPED_ACACIA_LOG);
        blockIds.put(155, Material.STRIPPED_BIRCH_LOG);
        blockIds.put(156, Material.STRIPPED_DARK_OAK_LOG);
        blockIds.put(157, Material.STRIPPED_JUNGLE_LOG);
        blockIds.put(158, Material.STRIPPED_OAK_LOG);
        blockIds.put(159, Material.STRIPPED_SPRUCE_LOG);
        blockIds.put(160, Material.TNT);
        blockIds.put(161, Material.TERRACOTTA);
        blockIds.put(162, Material.WET_SPONGE);
        blockIds.put(163, Material.WHITE_CONCRETE);
        blockIds.put(164, Material.WHITE_CONCRETE_POWDER);
        blockIds.put(165, Material.WHITE_TERRACOTTA);
        blockIds.put(166, Material.WHITE_WOOL);
        blockIds.put(167, Material.YELLOW_CONCRETE);
        blockIds.put(168, Material.YELLOW_TERRACOTTA);
        blockIds.put(169, Material.YELLOW_WOOL);
        blockIds.put(170, Material.YELLOW_CONCRETE_POWDER);

        ArrayList<Material> blocks = new ArrayList<Material>(Arrays.asList(Material.values()));
        blocks.removeIf((block) -> !block.isBlock());

        for (Material block : blocks) {
            blockNames.add(block.name().toLowerCase());
        }

        iniliazied = true;
    }

    public static Material getBlock(int id) {
        if (!iniliazied)
            init();

        if (!blockIds.containsKey(id))
            return Material.AIR;
        return blockIds.get(id);
    }

    public static int getId(Material material) {
        if (!iniliazied)
            init();

        if (!blockIds.containsValue(material))
            return -1;
        for (Map.Entry<Integer, Material> entry : blockIds.entrySet()) {
            if (material == entry.getValue())
                return entry.getKey();
        }
        return -1;
    }

    public static ArrayList<String> getBlockNames() {
        return blockNames;
    }
}
