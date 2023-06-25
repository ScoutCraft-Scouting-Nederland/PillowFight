package nl.scoutcraft.pillowfight.game.powerup;

import nl.scoutcraft.pillowfight.data.Keys;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Optional;

public class PillowExplosion extends Powerup {

    protected PillowExplosion(byte id) {
        super(Material.TNT, Locale.PILLOW_EXPLOSION_NAME, Locale.PILLOW_EXPLOSION_LORE, Locale.EMPTY, Locale.EMPTY, id, true, 200, 0);
    }

    @Override
    public void start(Player player) {
        player.getWorld().spawnParticle(Particle.CLOUD, player.getLocation(), 100, 0.3d, 0.3d, 0.3d);
        player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation(), 30, 0.5d, 0.5d, 0.5d);
        player.getWorld().spawnParticle(Particle.LAVA, player.getLocation(), 30, 0.5d, 0.5d, 0.5d);

        Location playerLoc = player.getLocation();
        int playerId = Optional.ofNullable(player.getPersistentDataContainer().get(Keys.TEAM_ID, PersistentDataType.INTEGER)).orElse(-1);

        playerLoc.getNearbyPlayers(10d).forEach(damaged -> {
            int damagedId = Optional.ofNullable(damaged.getPersistentDataContainer().get(Keys.TEAM_ID, PersistentDataType.INTEGER)).orElse(-2);

            if (damaged != player && !damaged.isInvisible() && !damaged.isInvulnerable() && playerId != damagedId) {
                double distance = playerLoc.distance(damaged.getLocation());
                double power = 5 / (0.04 * distance * distance + 1) - 1;

                damaged.setVelocity(damaged.getLocation().subtract(playerLoc).toVector().normalize().multiply(power).add(new Vector(0, 0.5, 0)));
                damaged.setKiller(player);
            }
        });
    }

    @Override
    public void stop(Player player) {

    }
}
