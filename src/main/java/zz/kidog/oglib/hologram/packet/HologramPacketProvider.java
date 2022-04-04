package zz.kidog.oglib.hologram.packet;

import zz.kidog.oglib.hologram.construct.HologramLine;
import org.bukkit.Location;

public interface HologramPacketProvider {

	HologramPacket getPacketsFor(Location location, HologramLine line);

	//TODO: add more versions
}
