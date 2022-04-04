package zz.kidog.oglib.command.defaults;

import zz.kidog.oglib.command.Command;
import zz.kidog.oglib.command.Param;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

public class EvalCommand {

    @Command(names = {"eval"}, permission= "console", description= "Evaluates a commands")
    public static void eval(CommandSender sender, @Param(name="commands", wildcard=true) String commandLine) {
        if (!(sender instanceof ConsoleCommandSender)) {
            sender.sendMessage(ChatColor.RED + "This is a console-only utility commands. It cannot be used from in-game.");
            return;
        }
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), commandLine);
    }

}

