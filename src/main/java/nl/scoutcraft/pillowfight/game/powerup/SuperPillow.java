package nl.scoutcraft.pillowfight.game.powerup;

import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SuperPillow extends Powerup {

    protected SuperPillow(byte id){
        super(Material.SLIME_BLOCK, Locale.SUPER_PILLOW_NAME, Locale.SUPER_PILLOW_LORE, Locale.SUPER_PILLOW_COUNTDOWN, Locale.SUPER_PILLOW_END, id, false, 120, 5);
    }

    @Override
    public void start(Player player) {

    }

    @Override
    public void stop(Player player) {

    }
}
