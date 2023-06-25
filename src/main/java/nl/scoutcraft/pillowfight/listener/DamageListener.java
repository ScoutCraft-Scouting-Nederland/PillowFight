package nl.scoutcraft.pillowfight.listener;

import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.data.Keys;
import nl.scoutcraft.pillowfight.game.powerup.Powerups;
import nl.scoutcraft.pillowfight.game.team.Teams;
import nl.scoutcraft.pillowfight.game.wrapper.PillowPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.Optional;

public class DamageListener implements Listener {

    private final double velocityMultiplier;

    public DamageListener(PillowFight plugin) {
        this.velocityMultiplier = plugin.getConfig().getDouble("VELOCITY");
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player) || !(event.getDamager() instanceof Player)) {
            event.setCancelled(true);
            return;
        }

        Player damager = (Player) event.getDamager();
        if (damager.isInvulnerable()) {
            event.setCancelled(true);
            return;
        }

        if (!Tag.WOOL.isTagged(damager.getInventory().getItemInMainHand().getType())) {
            event.setCancelled(true);
            return;
        }

        Player player = (Player) event.getEntity();
        int damagerId = Optional.ofNullable(damager.getPersistentDataContainer().get(Keys.TEAM_ID, PersistentDataType.INTEGER)).orElse(-1);
        int damagedId = Optional.ofNullable(player.getPersistentDataContainer().get(Keys.TEAM_ID, PersistentDataType.INTEGER)).orElse(-2);
        if (damagerId == damagedId) {
            event.setCancelled(true);
            return;
        }

        Vector velocity = damager.getLocation().getDirection().multiply(this.velocityMultiplier);
        if (Powerups.SUPER_PILLOW.isActive(damager))
            velocity.multiply(2.0);

        Location playerLocation = player.getLocation();
        if (player.isSneaking()) {
            player.teleport(playerLocation.add(0, 1.25, 0));
            Bukkit.getScheduler().runTaskLater(PillowFight.getInstance(), () -> player.setVelocity(velocity), 0L);
        } else {
            player.setVelocity(velocity);
        }

        event.setCancelled(false);
        event.setDamage(0d);
        player.setKiller(damager);

        player.getWorld().spawnParticle(Particle.FALLING_OBSIDIAN_TEAR, player.getEyeLocation(), 20, 1d, 1d, 1d);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getEyeLocation(), 80, 1d, 1d, 1d, new Particle.DustOptions(Teams.getColor(damagerId), 1.2f));

        PillowPlayer.get(player).addHit();
        PillowPlayer.get(damager).addSlap();
    }
}
