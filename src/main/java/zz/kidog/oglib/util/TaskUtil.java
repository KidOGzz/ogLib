package zz.kidog.oglib.util;

import org.bukkit.scheduler.BukkitRunnable;
import zz.kidog.oglib.ogLib;

public class TaskUtil {

    public static void run(Runnable runnable) {
        ogLib.getInstance().getServer().getScheduler().runTask(ogLib.getInstance(), runnable);
    }

    public static void runTimer(Runnable runnable, long delay, long timer) {
        ogLib.getInstance().getServer().getScheduler().runTaskTimer(ogLib.getInstance(), runnable, delay, timer);
    }

    public static void runTimer(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimer(ogLib.getInstance(), delay, timer);
    }

    public static void runTimerAsync(BukkitRunnable runnable, long delay, long timer) {
        runnable.runTaskTimerAsynchronously(ogLib.getInstance(), delay, timer);
    }

    public static void runLater(Runnable runnable, long delay) {
        ogLib.getInstance().getServer().getScheduler().runTaskLater(ogLib.getInstance(), runnable, delay);
    }

    public static void runAsync(Runnable runnable) {
        ogLib.getInstance().getServer().getScheduler().runTaskAsynchronously(ogLib.getInstance(), runnable);
    }

}

