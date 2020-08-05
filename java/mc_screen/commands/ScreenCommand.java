package mc_screen.commands;

import mc_screen.BlockUtils;
import mc_screen.Util;
import mc_screen.screen.Screen;
import mc_screen.screen.ScreenManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScreenCommand implements CommandExecutor, TabCompleter {

    private JavaPlugin plugin;
    private PluginCommand pluginCommand;
    private String name = "screen";

    public ScreenCommand(JavaPlugin plugin) {
        this.plugin = plugin;
        pluginCommand = plugin.getCommand(name);
        if (pluginCommand == null)
            return;

        pluginCommand.setExecutor(this);
        pluginCommand.setTabCompleter(this);
    }

    public boolean hasPermissions(CommandSender executor, String name) {
        return executor.hasPermission("screen.*") || executor.hasPermission("screen."+name) || executor.isOp();
    }

    @Override
    public boolean onCommand(CommandSender executor, Command command, String name, String[] args) {

        if (args[0].equalsIgnoreCase("create") || args[0].equalsIgnoreCase("add")) {
            if (!(executor instanceof  Player)) {
                executor.sendMessage("Only players can execute this command!");
                return true;
            }

            if (!hasPermissions(executor, "create")) {
                Util.sendErrorMessage(executor, "Insufficient permissions to execute this command! (missing: screen.create)");
                return true;
            }

            if (args.length < 2) {
                Util.sendErrorMessage(executor, "Usage: /screen create [width] <height>");
                return true;
            }

            int width;
            int height;

            if (args.length == 2) {
                if (!Util.isInteger(args[1])) {
                    Util.sendErrorMessage(executor, "Invalid screen size!");
                    return true;
                }

                width = Integer.parseInt(args[1]);
                height = (int)(width*0.56f);
            } else {
                if (!Util.isInteger(args[1]) || !Util.isInteger(args[2])) {
                    Util.sendErrorMessage(executor, "Invalid screen size!");
                    return true;
                }

                width = Integer.parseInt(args[1]);
                height = Integer.parseInt(args[2]);
            }


            Location location = ((Player) executor).getLocation();

            Screen screen = ScreenManager.createScreen(location, width, height, plugin);

            if (screen == null) {
                Util.sendErrorMessage(executor, "Too large screen: "+width*height+" blocks! (Max area: "+ScreenManager.MAX_AREA+")");
                return true;
            }

            TextComponent message = new TextComponent("["+screen.getToken()+"]");
            message.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, screen.getToken()));
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("[Copy token]").create() ));

            BaseComponent[] components = new ComponentBuilder("§aSuccessfully created screen! (token: ")
                    .append(message).underlined(true)
                    .append(")").underlined(false).create();

            ((Player)executor).spigot().sendMessage(components);
            return true;
        }
        else if (args[0].equalsIgnoreCase("delete") || args[0].equalsIgnoreCase("remove")) {
            if (!hasPermissions(executor, "delete")) {
                Util.sendErrorMessage(executor, "Insufficient permissions to execute this command! (missing: screen.delete)");
                return true;
            }

            if (args.length < 2) {
                Util.sendErrorMessage(executor, "Usage: /screen delete [token]");
                return true;
            }

            if (args[1].equals("all")) {
                for (String token : ScreenManager.getTokens()) {
                    ScreenManager.deleteScreen(token);
                }
                Util.sendSuccessMessage(executor, "Successfully deleted all screens");
                return true;
            } else if (ScreenManager.isValidToken(args[1])) {
                ScreenManager.deleteScreen(args[1]);
                Util.sendSuccessMessage(executor, "Successfully deleted screen with token: " + args[1]);
                return true;
            } else {
                Util.sendErrorMessage(executor, "Invalid screen token: " + args[1] + "!");
                return true;
            }

        }
        else if (args[0].equalsIgnoreCase("info")) {
            if (!hasPermissions(executor, "info")) {
                Util.sendErrorMessage(executor, "Insufficient permissions to execute this command! (missing: screen.delete)");
                return true;
            }

            if (args.length < 2) {
                Util.sendErrorMessage(executor, "Usage: /screen info [token|all]");
                return true;
            }

            if (args[1].equals("all")) {
                if (ScreenManager.getScreens().length == 0) {
                    Util.sendErrorMessage(executor, "No screen was created!");
                    return true;
                }
                Util.sendSuccessMessage(executor, "Screens info [" + ScreenManager.getScreens().length + "]:");
                for (Screen screen : ScreenManager.getScreens()) {
                    int x = screen.getX(); int y = screen.getY(); int z = screen.getZ();
                    int width = screen.getWidth();
                    int height = screen.getHeight();
                    int id = screen.getId();

                    String token = screen.getToken();

                    TextComponent token_text = new TextComponent("["+screen.getToken()+"]");
                    token_text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, screen.getToken()));
                    token_text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("[Copy token]").create()));

                    //"[x: "+x+", y: "+y+", z: "+z+"]"
                    TextComponent location_text = new TextComponent(
                            new ComponentBuilder("[x: ").color(ChatColor.GRAY)
                                    .append(String.valueOf(x)).color(ChatColor.BLUE)
                                    .append(", y: ").color(ChatColor.GRAY)
                                    .append(String.valueOf(y)).color(ChatColor.BLUE)
                                    .append(", z: ").color(ChatColor.GRAY)
                                    .append(String.valueOf(z)).color(ChatColor.BLUE)
                                    .append("]").color(ChatColor.GRAY).create());
                    location_text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp "+x+" "+(y + 1)+" "+z));
                    location_text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("[Tp to screen location]").create()));


                    BaseComponent[] components = new ComponentBuilder("Info for screen").color(ChatColor.GRAY)
                            .append(screen.getToken()).color(ChatColor.BLUE)
                            .append(", id: ").color(ChatColor.GRAY)
                            .append(String.valueOf(id)).color(ChatColor.AQUA)
                            .append(", token: ").color(ChatColor.GRAY)
                            .append(token_text).underlined(true).color(ChatColor.BLUE)
                            .append(", resolution: ").underlined(false).color(ChatColor.GRAY)
                            .append(String.valueOf(width)).color(ChatColor.AQUA)
                            .append("x").color(ChatColor.DARK_GRAY)
                            .append(String.valueOf(height)).color(ChatColor.AQUA)
                            .append(", position: ").color(ChatColor.GRAY)
                            .append(location_text).underlined(true)
                            .append("\n").underlined(false).create();

                    if (executor instanceof  Player)
                        ((Player) executor).spigot().sendMessage(components);
                    else
                        executor.sendMessage("Info for screen" + token + "id: " + id + ", token: " + token + ", resolution: " + width + "x" + height + ", position: [x:" + x + ", y: " + y + ", z: " + z + "]");
                }
                return true;
            }
            if (ScreenManager.isValidToken(args[1])) {
                Screen screen = ScreenManager.getScreen(args[1]);

                Util.sendSuccessMessage(executor, "Info for screen("+args[1]+")):");

                int x = screen.getX(); int y = screen.getY(); int z = screen.getZ();
                int width = screen.getWidth();
                int height = screen.getHeight();
                String token = screen.getToken();
                int id = screen.getId();

                TextComponent token_text = new TextComponent("["+screen.getToken()+"]");
                token_text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, screen.getToken()));
                token_text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("[Copy token]").create()));

                //"[x: "+x+", y: "+y+", z: "+z+"]"
                TextComponent location_text = new TextComponent(
                        new ComponentBuilder("[x: ").color(ChatColor.GRAY)
                                .append(String.valueOf(x)).color(ChatColor.BLUE)
                                .append(", y: ").color(ChatColor.GRAY)
                                .append(String.valueOf(y)).color(ChatColor.BLUE)
                                .append(", z: ").color(ChatColor.GRAY)
                                .append(String.valueOf(z)).color(ChatColor.BLUE)
                                .append("]").color(ChatColor.GRAY).create());
                location_text.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/tp "+x+" "+(y + 1)+" "+z));
                location_text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("[Tp to screen location]").create()));


                BaseComponent[] components = new ComponentBuilder("Info for screen").color(ChatColor.GRAY)
                        .append(screen.getToken()).color(ChatColor.BLUE)
                        .append(", id: ").color(ChatColor.GRAY)
                        .append(String.valueOf(id)).color(ChatColor.AQUA)
                        .append(", token: ").color(ChatColor.GRAY)
                        .append(token_text).underlined(true).color(ChatColor.BLUE)
                        .append(", resolution: ").underlined(false).color(ChatColor.GRAY)
                        .append(String.valueOf(width)).color(ChatColor.AQUA)
                        .append("x").color(ChatColor.DARK_GRAY)
                        .append(String.valueOf(height)).color(ChatColor.AQUA)
                        .append(", position: ").color(ChatColor.GRAY)
                        .append(location_text).underlined(true).create();


                if (executor instanceof  Player)
                    ((Player) executor).spigot().sendMessage(components);
                else
                    executor.sendMessage("Info for screen" + token + "id: " + id + ", token: " + token + ", resolution: " + width + "x" + height + ", position: [x:" + x + ", y: " + y + ", z: " + z + "]");

                return true;
            } else {
                Util.sendErrorMessage(executor, "Invalid screen token: " + args[1] + "!");
                return true;
            }

        }
        else if (args[0].equalsIgnoreCase("setpixel")) {
            if (!hasPermissions(executor, "edit")) {
                Util.sendErrorMessage(executor, "Insufficient permissions to execute this command! (missing: screen.edit)");
                return true;
            }
            if (args.length < 5) {
                Util.sendErrorMessage(executor, "Usage: /screen setpixel [token] [x] [y] [block]");
                return true;
            }

            if (!ScreenManager.isValidToken(args[1])) {
                Util.sendErrorMessage(executor, "Invalid screen token: " + args[1] + "!");
                return true;
            }

            if (!Util.isInteger(args[2]) || !Util.isInteger(args[3])) {
                Util.sendErrorMessage(executor, "Invalid screen coordinates!");
                return true;
            }

            Material block = Material.getMaterial(args[4].toUpperCase());
            if (block == null || !block.isBlock()) {
                Util.sendErrorMessage(executor, args[4] + "is not a valid block!");
                return true;
            }

            Screen screen = ScreenManager.getScreen(args[1]);

            int x = Integer.parseInt(args[2]); x = Math.min(x, screen.getWidth()-1);
            int y = Integer.parseInt(args[3]); y = Math.min(y, screen.getHeight()-1);

            screen.setPixel(x, y, block, true);
            Util.sendSuccessMessage(executor, "Successfully filled the screen with "+ args[4].toLowerCase());
            return true;

        }
        else if (args[0].equalsIgnoreCase("fill")) {
            if (!hasPermissions(executor, "edit")) {
                Util.sendErrorMessage(executor, "Insufficient permissions to execute this command! (missing: screen.edit)");
                return true;
            }
            if (args.length < 3) {
                Util.sendErrorMessage(executor, "Usage: /screen fill [token] [block]");
                return true;
            }

            if (!ScreenManager.isValidToken(args[1])) {
                Util.sendErrorMessage(executor, "Invalid screen token: " + args[1] + "!");
                return true;
            }

            Material block = Material.getMaterial(args[2].toUpperCase());
            if (block == null || !block.isBlock()) {
                Util.sendErrorMessage(executor, args[2] + "is not a valid block!");
                return true;
            }

            Screen screen = ScreenManager.getScreen(args[1]);

            screen.fill(block);

            Util.sendSuccessMessage(executor, "Successfully filled the screen with "+ args[2].toLowerCase());
            return true;
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender executor, Command command, String name, String[] args) {
        List<String> tabComplete = new ArrayList<String>();

        if (args.length == 1) {
            if (hasPermissions(executor, "create"))
                tabComplete.add("create");
            if (hasPermissions(executor, "delete"))
                tabComplete.add("delete");
            if (hasPermissions(executor, "info"))
                tabComplete.add("info");
            if (hasPermissions(executor, "edit")) {
                tabComplete.add("setpixel");
                tabComplete.add("fill");
            }

        }
        else if (args.length == 2) {
            String subCommand = args[0];
            if (subCommand.equalsIgnoreCase("delete") && hasPermissions(executor, "delete")) {
                tabComplete.add("all");
                tabComplete.addAll(Arrays.asList(ScreenManager.getTokens()));
            }
            else if (subCommand.equalsIgnoreCase("info") && hasPermissions(executor, "info")) {
                tabComplete.add("all");
                tabComplete.addAll(Arrays.asList(ScreenManager.getTokens()));
            }
            else if (subCommand.equalsIgnoreCase("setpixel") && hasPermissions(executor, "edit")) {
                tabComplete.addAll(Arrays.asList(ScreenManager.getTokens()));
            }
            else if (subCommand.equalsIgnoreCase("fill") && hasPermissions(executor, "edit")) {
                tabComplete.addAll(Arrays.asList(ScreenManager.getTokens()));
            }
        }
        else if (args.length == 3) {
            String subCommand = args[0];

            if (subCommand.equalsIgnoreCase("fill") && hasPermissions(executor, "edit")) {
                tabComplete.addAll(BlockUtils.getBlockNames());
            }
        }
        else if (args.length == 5) {
            String subCommand = args[0];

            if (subCommand.equalsIgnoreCase("setpixel") && hasPermissions(executor, "edit")) {
                tabComplete.addAll(BlockUtils.getBlockNames());
            }
        }

        tabComplete.removeIf((arg) -> !arg.toLowerCase().startsWith(args[args.length-1].toLowerCase()));
        return tabComplete;
    }
}
