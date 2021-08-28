package blue.athena.publicapi.module.board;

import blue.athena.publicapi.misc.java.AthenaString;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public abstract class AthenaBoard {

    @Getter private final Player player;
    @Getter @Setter private Scoreboard board;
    @Getter @Setter private Objective objective;

    @Getter @Setter private String name;
    @Getter @Setter private List<String> content;

    public AthenaBoard(Player player) {

        this.player = player;

        board = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = board.registerNewObjective("athenaBoard", "null");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        update();

    }

    public void setup(String name, List<String> content) {

        this.name = name;
        this.content = content;

    }

    public void show() {

        player.setScoreboard(board);

    }

    public void update() {

        process();

        objective.setDisplayName(name);

        int length = content.size();
        for (int i = length; i != 0; i--) {

            String text = content.get(i-1);
            objective.getScore(text).setScore(i);

        }

        show();

    }

    private void process() {

        this.name = AthenaString.fullyProcess(name, player);

        AthenaString.addColor(content);
        content = PlaceholderAPI.setPlaceholders(player, content);

    }

}
