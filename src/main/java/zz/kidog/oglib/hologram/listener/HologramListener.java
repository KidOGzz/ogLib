package zz.kidog.oglib.hologram.listener;


import zz.kidog.oglib.hologram.FrozenHologramHandler;
import zz.kidog.oglib.hologram.construct.Hologram;
import zz.kidog.oglib.hologram.type.BaseHologram;
import zz.kidog.oglib.ogLib;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class HologramListener implements Listener {

	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {

		ogLib.getInstance().getServer().getScheduler().runTaskLater(ogLib.getInstance(),() -> FrozenHologramHandler.getCache().forEach(hologram -> {

			final BaseHologram baseHologram = (BaseHologram) hologram;

			if ((baseHologram.getViewers() == null || baseHologram.getViewers().contains(event.getPlayer().getUniqueId()))
					&& baseHologram.getLocation().getWorld().equals(event.getPlayer().getWorld()) && hologram.getLocation().distance(event.getPlayer().getLocation()) <= 1600.0D) {

				baseHologram.show(event.getPlayer());
			}

		}),20L);

	}


	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerMove(PlayerMoveEvent event) {

		final Player player = event.getPlayer();

		final Location to = event.getTo();
		final Location from = event.getFrom();

		if (to.getBlockX() == from.getBlockX() && to.getBlockZ() == from.getBlockZ()) {
			return;
		}

		for (Hologram hologram : FrozenHologramHandler.getCache()) {

			final BaseHologram baseHologram = (BaseHologram)hologram;

			if ((baseHologram.getViewers() == null || baseHologram.getViewers().contains(event.getPlayer().getUniqueId())) && hologram.getLocation().getWorld().equals(event.getPlayer().getWorld())) {

				if (!baseHologram.getCurrentWatchers().contains(player.getUniqueId()) && hologram.getLocation().distanceSquared(player.getLocation()) <= 1600.0D) {
					baseHologram.show(player);
					continue;
				} if (baseHologram.getCurrentWatchers().contains(player.getUniqueId()) && hologram.getLocation().distanceSquared(player.getLocation()) > 1600.0D) {
					baseHologram.destroy0(player);
				}

			}
		}

	}


	@EventHandler(priority = EventPriority.NORMAL)
	public void onPlayerJoin(PlayerJoinEvent event) {

		for (Hologram hologram : FrozenHologramHandler.getCache()) {

			final BaseHologram baseHologram = (BaseHologram) hologram;

			if ((baseHologram.getViewers() == null || baseHologram.getViewers().contains(event.getPlayer().getUniqueId())) && baseHologram.getLocation().getWorld().equals(event.getPlayer().getWorld())) {
				baseHologram.show(event.getPlayer());
			}

		}
	}


	@EventHandler(priority = EventPriority.NORMAL)
	public void onRespawn(PlayerRespawnEvent event) {

		ogLib.getInstance().getServer().getScheduler().runTaskLater(ogLib.getInstance(),() -> {

			for (Hologram hologram : FrozenHologramHandler.getCache()) {

				final BaseHologram baseHologram = (BaseHologram) hologram;

				baseHologram.destroy0(event.getPlayer());

				if ((baseHologram.getViewers() == null || baseHologram.getViewers().contains(event.getPlayer().getUniqueId())) && baseHologram.getLocation().getWorld().equals(event.getPlayer().getWorld())) {
					baseHologram.show(event.getPlayer());
				}
			}

		},10L);

	}


}
