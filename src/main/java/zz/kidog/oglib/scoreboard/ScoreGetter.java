package zz.kidog.oglib.scoreboard;

import zz.kidog.oglib.util.LinkedList;
import org.bukkit.entity.Player;

public interface ScoreGetter {

    void getScores(LinkedList<String> var1, Player var2);

}

