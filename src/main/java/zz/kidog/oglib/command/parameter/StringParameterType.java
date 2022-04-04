package zz.kidog.oglib.command.parameter;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Set;

import zz.kidog.oglib.command.ParameterType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StringParameterType implements ParameterType<String> {

    @Override
    public String transform(CommandSender sender, String value) {
        return value;
    }

    @Override
    public List<String> tabComplete(Player sender, Set<String> flags, String prefix) {
        return ImmutableList.of();
    }
}

