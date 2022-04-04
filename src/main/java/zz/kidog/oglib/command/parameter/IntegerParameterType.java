package zz.kidog.oglib.command.parameter;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Set;

import zz.kidog.oglib.command.ParameterType;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IntegerParameterType implements ParameterType<Integer> {

    @Override
    public Integer transform(CommandSender sender, String value) {
        try {
            return Integer.parseInt(value);
        }
        catch (NumberFormatException exception) {
            sender.sendMessage(ChatColor.RED + value + " is not a valid number.");
            return null;
        }
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String prefix) {
        return ImmutableList.of();
    }
}

