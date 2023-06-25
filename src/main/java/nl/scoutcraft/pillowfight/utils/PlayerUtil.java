package nl.scoutcraft.pillowfight.utils;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class PlayerUtil {

    public static void setHealth(Player player, double health, boolean removeAbsorption) {
        if (player.getHealth() > health) {
            player.setHealth(health);
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
        } else {
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
            player.setHealth(health);
        }

        if (removeAbsorption) player.setAbsorptionAmount(0);
    }
}
