package nl.scoutcraft.pillowfight.game.powerup;

import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ExtraLife extends Powerup {

    protected ExtraLife(byte id) {
        super(Material.GOLDEN_APPLE, Locale.EXTRA_LIFE_NAME, Locale.EXTRA_LIFE_LORE, Locale.EMPTY, Locale.EMPTY, id, true, 100, 0);
    }

    @Override
    public void start(Player player) {
        player.setAbsorptionAmount(player.getAbsorptionAmount() + 2D);
    }

    @Override
    public void stop(Player player) {

    }
}
