package zz.kidog.oglib.protocol;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.EnumWrappers;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import zz.kidog.oglib.ogLib;
import zz.kidog.oglib.protocol.event.PlayerCloseInventoryEvent;
import zz.kidog.oglib.protocol.event.PlayerOpenInventoryEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class InventoryAdapter extends PacketAdapter {

    private static final Set<UUID> currentlyOpen = new HashSet<>();

    public InventoryAdapter() {
        super(ogLib.getInstance(), PacketType.Play.Client.CLIENT_COMMAND, PacketType.Play.Client.CLOSE_WINDOW);
    }

    public void onPacketReceiving(PacketEvent event) {
        Player player = event.getPlayer();
        PacketContainer packet = event.getPacket();
        if (packet.getType() == PacketType.Play.Client.CLIENT_COMMAND && packet.getClientCommands().size() != 0 && packet.getClientCommands().read(0) == EnumWrappers.ClientCommand.OPEN_INVENTORY_ACHIEVEMENT) {
            if (!currentlyOpen.contains(player.getUniqueId())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(ogLib.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerOpenInventoryEvent(player)));
            }
            currentlyOpen.add(player.getUniqueId());
        } else if (packet.getType() == PacketType.Play.Client.CLOSE_WINDOW) {
            if (currentlyOpen.contains(player.getUniqueId())) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(ogLib.getInstance(), () -> Bukkit.getPluginManager().callEvent(new PlayerCloseInventoryEvent(player)));
            }
            currentlyOpen.remove(player.getUniqueId());
        }
    }

    public static Set<UUID> getCurrentlyOpen() {
        return currentlyOpen;
    }
}

