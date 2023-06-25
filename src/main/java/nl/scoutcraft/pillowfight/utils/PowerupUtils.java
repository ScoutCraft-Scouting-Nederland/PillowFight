package nl.scoutcraft.pillowfight.utils;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.ItemLine;
import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.data.Keys;
import nl.scoutcraft.pillowfight.game.powerup.Powerup;
import nl.scoutcraft.pillowfight.game.wrapper.PillowPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;

public class PowerupUtils {

    public static void spawnHologram(final PillowFight pillowfight, final Powerup powerup, final Area area) {
        Location loc = getRandomLocation(area);
        if (loc == null) return;

        final Hologram hologram = HologramsAPI.createHologram(pillowfight, loc.add(0.0, 1, 0.0));
        hologram.appendTextLine(powerup.getName().get((Locale) null, true));
        ItemLine itemLine = hologram.appendItemLine(new ItemStack(powerup.getType()));

        itemLine.setPickupHandler(player -> {
            if (player.isInvisible()) return;

            hologram.delete();

            player.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, hologram.getLocation(), 100, 0.6d, 0.6d, 0.6d);
            player.getWorld().spawnParticle(Particle.SMOKE_LARGE, hologram.getLocation(), 50, 0.4d, 0.4d, 0.4d);
            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, hologram.getLocation(), 30, 0.5d, 0.5d, 0.5d);
            player.playSound(hologram.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.95F);

            PowerupUtils.give(player, powerup);
            PillowPlayer.get(player).addPowerupPickup();
        });
    }

    // TODO: Improve
    @Nullable
    private static Location getRandomLocation(Area area) {
        World world = Bukkit.getWorld(area.getWorld());

        Material ground;
        Location location;

        int count = 0;
        while (true) {
            location = new Location(world,
                    area.getMinX() + (area.getMaxX() - area.getMinX()) * Math.random(),
                    area.getMinY() + (area.getMaxY() - area.getMinY()) * Math.random(),
                    area.getMinZ() + (area.getMaxZ() - area.getMinZ()) * Math.random()
            ).toBlockLocation().add(0.5, 0.0, 0.5);
            ground = location.getBlock().getType();

            if (ground != Material.AIR) continue;

            while (ground == Material.AIR && location.getY() > area.getMinY()) {
                location = location.subtract(0, 1, 0);
                ground = location.getBlock().getType();
            }

            if (ground.isSolid() && !ground.equals(Material.BARRIER)) break;

            if (count++ >= 100) return null;
        }

        location.setY(location.getBlock().getBoundingBox().getMaxY());
        return location;
    }

    public static void give(final Player player, final Powerup powerup) {
        PlayerInventory inv = player.getInventory();

        for (int i = 0; i < 9; i++) {
            ItemStack item = inv.getItem(i);
            if (item == null || item.getItemMeta() == null) continue;

            Byte powerupId = item.getItemMeta().getPersistentDataContainer().get(Keys.POWERUP_ID, PersistentDataType.BYTE);
            if (powerupId != null && powerup.getId() == powerupId) {
                item.setAmount(item.getAmount() + 1);
                inv.setItem(i, item);
                return;
            }
        }

        powerup.getButton().apply(player);
    }
}
