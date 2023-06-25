package nl.scoutcraft.pillowfight.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import nl.scoutcraft.eagle.api.locale.Placeholder;
import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.data.Metrics;
import nl.scoutcraft.pillowfight.game.wrapper.PillowPlayer;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GGListener implements Listener {

    private static final int KARMA = 1;

    private final PillowFight plugin;
    private final List<UUID> karmaGiven;

    public GGListener(PillowFight plugin) {
        this.plugin = plugin;
        this.karmaGiven = new ArrayList<>();
    }

    @EventHandler
    public void onChat(AsyncChatEvent e) {
        String msg = e.message().toString();
        Player player = e.getPlayer();
        UUID uuid = player.getUniqueId();

        // Checking if the place != 0 ensures no spectators can get karma
        if (msg.toLowerCase().contains("gg") && !this.karmaGiven.contains(uuid) && PillowPlayer.get(uuid).getPlace() != 0) {
            this.karmaGiven.add(uuid);
            Metrics.addKarma(uuid, KARMA);

            Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                Locale.KARMA.send(player, new Placeholder("%karma%", KARMA + ""));
                player.playSound(player.getEyeLocation(), Sound.BLOCK_ANVIL_FALL, 1.0f, 1.0f);
            }, 0L);
        }
    }
}
