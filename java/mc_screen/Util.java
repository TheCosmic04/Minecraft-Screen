package mc_screen;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {

    public static boolean isInteger(String string) {
        try {
            Integer.parseInt(string);
        } catch(NumberFormatException | NullPointerException err) {
            return false;
        }

        return true;
    }

    public static void sendErrorMessage(CommandSender executor, String message) {
        if (executor instanceof Player)
            ((Player) executor).sendMessage(ChatColor.RED + message);
        else
            executor.sendMessage(message);
    }

    public static void sendSuccessMessage(CommandSender executor, String message) {
        if (executor instanceof Player)
            ((Player) executor).sendMessage(ChatColor.GREEN + message);
        else
            executor.sendMessage(message);
    }

    public static void sendMessage(CommandSender executor, String message) {
        executor.sendMessage(message);
    }

}
