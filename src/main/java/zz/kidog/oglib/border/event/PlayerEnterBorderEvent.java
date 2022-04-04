package zz.kidog.oglib.border.event;

import zz.kidog.oglib.border.Border;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerEnterBorderEvent extends PlayerBorderEvent {

    private final Location from;
    private final Location to;

    public PlayerEnterBorderEvent(Border border, Player player, Location from, Location to) {
        super(border, player);
        this.from = from;
        this.to = to;
    }

    public Location getFrom() {
        return this.from;
    }

    public Location getTo() {
        return this.to;
    }
}

