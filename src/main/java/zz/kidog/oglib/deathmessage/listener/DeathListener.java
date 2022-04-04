package zz.kidog.oglib.deathmessage.listener;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import zz.kidog.oglib.ogLib;
import zz.kidog.oglib.deathmessage.DeathMessageConfiguration;
import zz.kidog.oglib.deathmessage.FrozenDeathMessageHandler;
import zz.kidog.oglib.deathmessage.damage.Damage;
import zz.kidog.oglib.deathmessage.damage.PlayerDamage;
import zz.kidog.oglib.deathmessage.damage.UnknownDamage;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public final class DeathListener implements Listener {

    @EventHandler(priority=EventPriority.LOWEST)
    public void onPlayerDeathEarly(PlayerDeathEvent event) {
        Damage deathCause;
        List<Damage> record = FrozenDeathMessageHandler.getDamage(event.getEntity());
        if (!record.isEmpty() && (deathCause = record.get(record.size() - 1)) instanceof PlayerDamage && deathCause.getTimeAgoMillis() < TimeUnit.MINUTES.toMillis(1L)) {
            UUID killerUuid = ((PlayerDamage)deathCause).getDamager();
            Player killerPlayer = ogLib.getInstance().getServer().getPlayer(killerUuid);
            if (killerPlayer != null) {
                ((CraftPlayer)event.getEntity()).getHandle().killer = ((CraftPlayer)killerPlayer).getHandle();
            }
        }
    }

    @EventHandler(priority=EventPriority.MONITOR)
    public void onPlayerDeathLate(PlayerDeathEvent event) {
        List<Damage> record = FrozenDeathMessageHandler.getDamage(event.getEntity());
        Damage deathCause = !record.isEmpty() ? record.get(record.size() - 1) : new UnknownDamage(event.getEntity().getUniqueId(), 1.0);
        FrozenDeathMessageHandler.clearDamage(event.getEntity());
        event.setDeathMessage(null);
        DeathMessageConfiguration configuration = FrozenDeathMessageHandler.getConfiguration();
        UUID diedUuid = event.getEntity().getUniqueId();
        UUID killerUuid = event.getEntity().getKiller() == null ? null : event.getEntity().getKiller().getUniqueId();
        for (Player player : ogLib.getInstance().getServer().getOnlinePlayers()) {
            boolean showDeathMessage = configuration.shouldShowDeathMessage(player.getUniqueId(), diedUuid, killerUuid);
            if (!showDeathMessage) continue;
            String deathMessage = deathCause.getDeathMessage(player.getUniqueId());
            player.sendMessage(deathMessage);
        }
    }

}

