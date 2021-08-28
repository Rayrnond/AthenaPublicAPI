package blue.athena.publicapi.module.board;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class AnimatedBoard extends AthenaBoard {

    @Getter private final List<Scoreboard> frames;

    @Getter @Setter private boolean active;

    public AnimatedBoard(Player player) {

        super(player);

        frames = new ArrayList<>();

    }

}
