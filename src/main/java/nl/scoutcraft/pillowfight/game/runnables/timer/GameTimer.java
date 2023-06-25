package nl.scoutcraft.pillowfight.game.runnables.timer;

import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.manager.ScoreboardManager;
import org.bukkit.scheduler.BukkitRunnable;

public class GameTimer extends BukkitRunnable {

    private final PillowFight plugin;

    public int seconds = 0;

    public GameTimer(PillowFight plugin) {
        this.plugin = plugin;
    }

    public void start() {
        runTaskTimer(plugin, 0L, 20L);
    }

    @Override
    public void run() {
        ScoreboardManager boards = this.plugin.getBoardManager();
        this.plugin.getGameManager().getPlayers().forEach(p -> boards.update(p, ScoreboardManager.TIME, this.seconds));

        this.seconds++;
    }
}
