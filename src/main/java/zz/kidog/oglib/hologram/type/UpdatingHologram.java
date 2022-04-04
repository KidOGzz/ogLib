package zz.kidog.oglib.hologram.type;

import zz.kidog.oglib.hologram.builder.UpdatingHologramBuilder;
import zz.kidog.oglib.hologram.construct.Hologram;
import zz.kidog.oglib.ogLib;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public final class UpdatingHologram extends BaseHologram {

	private long interval;

	private Consumer<Hologram> updateFunction;
	private boolean showing = false;

	public UpdatingHologram(UpdatingHologramBuilder builder) {
		super(builder);

		this.interval = builder.getInterval();
		this.updateFunction = builder.getUpdateFunction();
	}


	public void send() {

		if (this.showing) {
			this.update();
			return;
		}

		super.send();

		this.showing = true;

		ogLib.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(ogLib.getInstance(),new BukkitRunnable() {
			@Override
			public void run() {

				if (showing) {
					update();
				} else {
					cancel();
				}

			}
		},0L,this.interval*20L);

	}

	public void destroy() {
		super.destroy();
		this.showing = false;
	}

	public void update() {
		this.updateFunction.accept(this);
		super.update();
	}

}
