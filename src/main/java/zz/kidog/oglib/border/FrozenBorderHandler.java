package zz.kidog.oglib.border;

import com.google.common.base.Preconditions;
import zz.kidog.oglib.ogLib;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;

public class FrozenBorderHandler {

    private static final Map<World, Border> borderMap = new HashMap<>();
    private static boolean initiated = false;

    private FrozenBorderHandler() {
    }

    public static void init() {
        Preconditions.checkState(!initiated);
        initiated = true;
        Bukkit.getPluginManager().registerEvents(new BorderListener(), ogLib.getInstance());
        Bukkit.getPluginManager().registerEvents(new InternalBorderListener(), ogLib.getInstance());
        new EnsureInsideRunnable().runTaskTimer(ogLib.getInstance(), 5L, 5L);
    }

    public static Border getBorderForWorld(World world) {
        return borderMap.get(world);
    }

    static void addBorder(Border border) {
        borderMap.put(border.getOrigin().getWorld(), border);
    }
}

