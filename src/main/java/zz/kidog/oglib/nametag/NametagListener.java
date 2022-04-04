package zz.kidog.oglib.nametag;

import zz.kidog.oglib.ogLib;
import lombok.NoArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;

@NoArgsConstructor
public final class NametagListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (FrozenNametagHandler.isInitiated()) {
            event.getPlayer().setMetadata("ogLibNametag-LoggedIn", new FixedMetadataValue(ogLib.getInstance(), true));
            FrozenNametagHandler.initiatePlayer(event.getPlayer());
            FrozenNametagHandler.reloadPlayer(event.getPlayer());
            FrozenNametagHandler.reloadOthersFor(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.getPlayer().removeMetadata("ogLibNametag-LoggedIn", ogLib.getInstance());
        FrozenNametagHandler.getTeamMap().remove(event.getPlayer().getUniqueId());
    }
}
