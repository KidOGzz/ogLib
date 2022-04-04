package zz.kidog.oglib.autoreboot.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import zz.kidog.oglib.autoreboot.AutoRebootHandler;
import zz.kidog.oglib.autoreboot.SilentAutoRebootHandler;
import zz.kidog.oglib.command.Command;
import zz.kidog.oglib.command.Param;
import zz.kidog.oglib.util.TimeUtils;

import java.util.concurrent.TimeUnit;

public class SilentAutoRebootCommand {

    @Command(names={"sreboot"}, permission="server.reboot")
    public static void reboot(CommandSender sender, @Param(name="time") String unparsedTime) {
            unparsedTime = unparsedTime.toLowerCase();
            int time = TimeUtils.parseTime(unparsedTime);
            SilentAutoRebootHandler.rebootServer(time, TimeUnit.SECONDS);
            sender.sendMessage(ChatColor.YELLOW + "Started auto reboot.");
    }

    @Command(names={"sreboot cancel"}, permission="server.reboot")
    public static void rebootCancel(CommandSender sender) {
        if (!SilentAutoRebootHandler.isRebooting()) {
            sender.sendMessage(ChatColor.RED + "No reboot has been scheduled.");
        } else {
            AutoRebootHandler.cancelReboot();
        }
    }

}

