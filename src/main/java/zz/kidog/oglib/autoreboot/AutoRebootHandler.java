package zz.kidog.oglib.autoreboot;

import com.google.common.base.Preconditions;
import zz.kidog.oglib.autoreboot.listeners.AutoRebootListener;
import zz.kidog.oglib.autoreboot.tasks.ServerRebootTask;
import zz.kidog.oglib.ogLib;
import zz.kidog.oglib.command.FrozenCommandHandler;

import java.util.List;
import java.util.concurrent.TimeUnit;

public final class AutoRebootHandler {

    private static List<Integer> rebootTimes;
    private static boolean initiated = false;
    private static ServerRebootTask serverRebootTask = null;

    private AutoRebootHandler() {
    }

    public static void init() {
        Preconditions.checkState(!initiated);
        initiated = true;
        FrozenCommandHandler.registerPackage(ogLib.getInstance(), "zz.kidog.oglib.autoreboot.commands");
        rebootTimes = ogLib.getInstance().getConfig().getIntegerList("AutoRebootTimes");
        ogLib.getInstance().getServer().getPluginManager().registerEvents(new AutoRebootListener(), ogLib.getInstance());
    }

    @Deprecated
    public static void rebootServer(int seconds) {
        AutoRebootHandler.rebootServer(seconds, TimeUnit.SECONDS);
    }

    public static void rebootServer(int timeUnitAmount, TimeUnit timeUnit) {
        if (serverRebootTask != null) {
            throw new IllegalStateException("Reboot already in progress");
        }
        serverRebootTask = new ServerRebootTask(timeUnitAmount, timeUnit);
        serverRebootTask.runTaskTimer(ogLib.getInstance(), 20L, 20L);
    }

    public static boolean isRebooting() {
        return serverRebootTask != null;
    }

    public static int getRebootSecondsRemaining() {
        if (serverRebootTask == null) {
            return -1;
        }
        return serverRebootTask.getSecondsRemaining();
    }

    public static void cancelReboot() {
        if (serverRebootTask != null) {
            serverRebootTask.cancel();
            serverRebootTask = null;
        }
    }

    public static List<Integer> getRebootTimes() {
        return rebootTimes;
    }

    public static boolean isInitiated() {
        return initiated;
    }

}

