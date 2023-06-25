package nl.scoutcraft.pillowfight.game.powerup;

import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class Dash extends Powerup {

    protected Dash(byte id) {
        super(Material.SUGAR, Locale.DASH_NAME, Locale.DASH_LORE, Locale.EMPTY, Locale.EMPTY, id, true, 100, 0);
    }

    @Override
    public void start(Player player) {
        //player.setVelocity(player.getVelocity().multiply(5));
        player.setVelocity(player.getLocation().getDirection().multiply(1.2d));
    }

    @Override
    public void stop(Player player) {

    }
}
