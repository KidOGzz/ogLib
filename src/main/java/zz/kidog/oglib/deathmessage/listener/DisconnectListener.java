package zz.kidog.oglib.deathmessage.listener;

import zz.kidog.oglib.deathmessage.FrozenDeathMessageHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public final class DisconnectListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        FrozenDeathMessageHandler.clearDamage(event.getPlayer());
    }

}

