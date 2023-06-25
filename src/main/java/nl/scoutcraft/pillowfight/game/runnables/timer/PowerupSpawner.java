package nl.scoutcraft.pillowfight.game.runnables.timer;

import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.game.powerup.Powerups;
import nl.scoutcraft.pillowfight.utils.Area;
import nl.scoutcraft.pillowfight.utils.PowerupUtils;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class PowerupSpawner extends BukkitRunnable {

    private final PillowFight plugin;
    private BukkitTask task;

    private final Area area;
    private final int interval;
    private int seconds;

    public PowerupSpawner(PillowFight plugin, Area area, int interval) {
        this.plugin = plugin;
        this.area = area;
        this.interval = interval;
    }

    public void start() {
        this.task = runTaskTimer(this.plugin, this.interval, this.interval);
    }

    public void stop() {
        if (this.task != null) {
            this.task.cancel();
            this.task = null;
        }
    }

    @Override
    public void run() {
        if (this.seconds <= 0) {
            this.seconds = interval;

            PowerupUtils.spawnHologram(this.plugin, Powerups.random(), this.area);
        }

        this.seconds--;
    }
}
