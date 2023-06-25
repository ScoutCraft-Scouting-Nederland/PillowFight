package nl.scoutcraft.pillowfight.listener;

import nl.scoutcraft.pillowfight.game.powerup.Powerups;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;

public class DoubleJumpListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if (p.getAllowFlight() || !canDoubleJump(p))
            return;

        Material ground = p.getLocation().subtract(0d, 0.5d, 0d).getBlock().getType();
        if (ground != Material.AIR && ground != Material.VOID_AIR)
            p.setAllowFlight(true);
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent e) {
        Player p = e.getPlayer();

        if (canDoubleJump(p)) {
            e.setCancelled(true);
            p.setFlying(false);
            p.setAllowFlight(false);

            p.setVelocity(p.getVelocity().setY(1d));
            p.getWorld().spawnParticle(Particle.CLOUD, p.getLocation(), 20, 4d, 1d, 1d, 20);
        }
    }

    private boolean canDoubleJump(final Player player) {
        return Powerups.DOUBLE_JUMP.isActive(player);
    }
}
