package zz.kidog.oglib.command.defaults;

import zz.kidog.oglib.ogLib;
import zz.kidog.oglib.command.Command;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class BuildCommand {

    @Command(names={"build"}, permission="oglib.build")
    public static void build(Player sender) {
        if (sender.hasMetadata("build")) {
            sender.removeMetadata("build", ogLib.getInstance());
        } else {
            sender.setMetadata("build", new FixedMetadataValue(ogLib.getInstance(), true));
        }
        sender.sendMessage(ChatColor.YELLOW + "You are " + (sender.hasMetadata("build") ? ChatColor.GREEN + "now" : ChatColor.RED + "no longer") + ChatColor.YELLOW + " in build mode.");
    }

}

