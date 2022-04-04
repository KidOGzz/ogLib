package zz.kidog.oglib.scoreboard;

import zz.kidog.oglib.ogLib;
import org.bukkit.entity.Player;

public final class ScoreboardThread extends Thread {

    public ScoreboardThread() {
        super("ogLib - Scoreboard Thread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            for (final Player online : ogLib.getInstance().getServer().getOnlinePlayers()) {
                try {
                    FrozenScoreboardHandler.updateScoreboard(online);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(FrozenScoreboardHandler.getUpdateInterval() * 50L);
            }
            catch (InterruptedException e2) {
                e2.printStackTrace();
            }
        }
    }
}