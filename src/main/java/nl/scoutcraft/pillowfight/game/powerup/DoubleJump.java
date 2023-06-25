package nl.scoutcraft.pillowfight.game.powerup;

import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DoubleJump extends Powerup {

    protected DoubleJump(byte id) {
        super(Material.RABBIT_FOOT, Locale.DOUBLE_JUMP_NAME, Locale.DOUBLE_JUMP_LORE, Locale.DOUBLE_JUMP_COUNTDOWN, Locale.DOUBLE_JUMP_END, id, false, 220, 10);
    }

    @Override
    public void start(Player player) {

    }

    @Override
    public void stop(Player player) {
        player.setAllowFlight(false);
    }
}
