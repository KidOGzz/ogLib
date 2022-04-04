package zz.kidog.oglib.autoreboot.tasks;

import java.util.concurrent.TimeUnit;

import zz.kidog.oglib.ogLib;
import zz.kidog.oglib.util.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public final class ServerRebootTask extends BukkitRunnable {

    private int secondsRemaining;
    private final boolean wasWhitelisted;

    public ServerRebootTask(int timeUnitAmount, TimeUnit timeUnit) {
        this.secondsRemaining = (int)timeUnit.toSeconds(timeUnitAmount);
        this.wasWhitelisted = ogLib.getInstance().getServer().hasWhitelist();
    }

    public void run() {
        if (this.secondsRemaining == 300) {
            ogLib.getInstance().getServer().setWhitelist(true);
        } else if (this.secondsRemaining == 0) {
            ogLib.getInstance().getServer().setWhitelist(this.wasWhitelisted);
            ogLib.getInstance().getServer().shutdown();
        }
        switch (this.secondsRemaining) {
            case 5: 
            case 10: 
            case 15: 
            case 30: 
            case 60: 
            case 120: 
            case 180: 
            case 240: 
            case 300: {
                ogLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
                ogLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "Server rebooting in " + TimeUtils.formatIntoDetailedString(this.secondsRemaining) + ".");
                ogLib.getInstance().getServer().broadcastMessage(ChatColor.RED + "\u26a0 " + ChatColor.DARK_RED + ChatColor.STRIKETHROUGH + "------------------------" + ChatColor.RED + " \u26a0");
                break;
            }
        }
        --this.secondsRemaining;
    }

    public synchronized void cancel() throws IllegalStateException {
        super.cancel();
        Bukkit.setWhitelist(this.wasWhitelisted);
    }

    public int getSecondsRemaining() {
        return this.secondsRemaining;
    }
}

