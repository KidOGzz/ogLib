package zz.kidog.oglib.protocol;

import zz.kidog.oglib.protocol.event.ServerLaggedOutEvent;
import zz.kidog.oglib.util.PlayerUtils;
import net.minecraft.util.com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class LagCheck extends BukkitRunnable {

    public void run() {
        ImmutableList<Player> players = ImmutableList.copyOf(Bukkit.getOnlinePlayers());
        if (players.size() >= 100) {
            int playersLagging = 0;
            for (Player player : players) {
                if (!PlayerUtils.isLagging(player)) continue;
                ++playersLagging;
            }

            double percentage = playersLagging * 100 / players.size();

            if (Math.abs(percentage) >= 30.0) {
                Bukkit.getPluginManager().callEvent(new ServerLaggedOutEvent(PingAdapter.getAveragePing()));
            }
        }
    }
}

