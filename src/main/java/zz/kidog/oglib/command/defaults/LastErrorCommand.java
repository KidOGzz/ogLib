package zz.kidog.oglib.command.defaults;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import zz.kidog.oglib.command.Command;
import zz.kidog.oglib.ogLib;
import zz.kidog.oglib.util.TimeUtils;

import java.util.Date;

public class LastErrorCommand {

    @Command(names = "lasterror", permission = "oglib.lasterror", hidden = true)
    public static void lastError(Player player) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&5&m---------------------------------------------"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&6oglib: &7❘ &4Error Management"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&7&m---------------------------------------------"));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&eLocal Redis Error: &c" + TimeUtils.formatIntoCalendarString(new Date(ogLib.getInstance().getLocalRedisLastError()))));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&eBackbone Redis Error: &c" + TimeUtils.formatIntoCalendarString(new Date(ogLib.getInstance().getBackboneRedisLastError()))));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&eCommand Error: &c" + TimeUtils.formatIntoCalendarString(new Date(ogLib.getInstance().getLocalRedisLastError()))));
        player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&5&m---------------------------------------------"));
    }

}
