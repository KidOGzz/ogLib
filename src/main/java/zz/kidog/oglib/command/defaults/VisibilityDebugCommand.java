package zz.kidog.oglib.command.defaults;

import java.util.List;

import zz.kidog.oglib.visibility.FrozenVisibilityHandler;
import zz.kidog.oglib.command.Command;
import zz.kidog.oglib.command.Param;
import net.minecraft.util.com.google.common.collect.Iterables;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class VisibilityDebugCommand {

    @Command(names = {"visibilitydebug", "debugvisibility", "visdebug", "cansee"}, permission=  "")
    public static void visibilityDebug(Player sender, @Param(name="viewer") Player viewer, @Param(name="target") Player target) {
        boolean bukkit;
        List<String> lines = FrozenVisibilityHandler.getDebugInfo(target, viewer);
        for (String debugLine : lines) {
            sender.sendMessage(debugLine);
        }
        boolean shouldBeAbleToSee = false;
        if (!Iterables.getLast(lines).contains("cannot")) {
            shouldBeAbleToSee = true;
        }
        if (shouldBeAbleToSee != viewer.canSee(target)) {
            sender.sendMessage(ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Updating was not done correctly: " + viewer.getName() + " should be able to see " + target.getName() + " but cannot.");
        } else {
            sender.sendMessage(ChatColor.GREEN + "Bukkit currently respects this result.");
        }
    }

}

