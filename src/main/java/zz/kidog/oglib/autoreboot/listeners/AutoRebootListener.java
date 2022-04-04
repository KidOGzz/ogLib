package zz.kidog.oglib.autoreboot.listeners;

import java.util.concurrent.TimeUnit;

import zz.kidog.oglib.autoreboot.AutoRebootHandler;
import zz.kidog.oglib.event.HourEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AutoRebootListener implements Listener {

    @EventHandler
    public void onHour(HourEvent event) {
        if (AutoRebootHandler.getRebootTimes().contains(event.getHour())) {
            AutoRebootHandler.rebootServer(5, TimeUnit.MINUTES);
        }
    }

}

