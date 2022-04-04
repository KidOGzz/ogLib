package zz.kidog.oglib.autoreboot.commands;

import java.util.concurrent.TimeUnit;

import zz.kidog.oglib.ogLib;
import zz.kidog.oglib.autoreboot.AutoRebootHandler;
import zz.kidog.oglib.util.TimeUtils;
import zz.kidog.oglib.command.Command;
import zz.kidog.oglib.command.Param;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class AutoRebootCommand {

    @Command(names={"reboot"}, permission="server.reboot")
    public static void reboot(CommandSender sender, @Param(name="time") String unparsedTime) {
        try {
            unparsedTime = unparsedTime.toLowerCase();
            int time = TimeUtils.parseTime(unparsedTime);
            AutoRebootHandler.rebootServer(time, TimeUnit.SECONDS);
            sender.sendMessage(ChatColor.YELLOW + "Started auto reboot.");
        }
        catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + ex.getMessage());
        }
    }

    @Command(names={"reboot cancel"}, permission="server.reboot")
    public static void rebootCancel(CommandSender sender) {
        if (!AutoRebootHandler.isRebooting()) {
            sender.sendMessage(ChatColor.RED + "No reboot has been scheduled.");
        } else {
            AutoRebootHandler.cancelReboot();
            ogLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
            ogLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "The server reboot has been cancelled.");
            ogLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
        }
    }

}

