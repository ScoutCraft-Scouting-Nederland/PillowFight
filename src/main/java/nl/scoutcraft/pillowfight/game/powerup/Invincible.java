package nl.scoutcraft.pillowfight.game.powerup;

import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Invincible extends Powerup {

    protected Invincible(byte id) {
        super(Material.NETHER_STAR, Locale.INVINCIBLE_NAME, Locale.INVINCIBLE_LORE, Locale.INVINCIBLE_COUNTDOWN, Locale.INVINCIBLE_END, id, false, 120, 5);
    }

    public void start(Player player) {
        player.setInvulnerable(true);
        player.setGlowing(true);
    }

    public void stop(Player player) {
        player.setInvulnerable(false);
        player.setGlowing(false);
    }
}
