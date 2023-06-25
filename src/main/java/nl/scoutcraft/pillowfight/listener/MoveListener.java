package nl.scoutcraft.pillowfight.listener;

import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.game.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MoveListener implements Listener {

    private final GameManager gameManager;
    private final int voidY;

    public MoveListener(PillowFight plugin) {
        this.gameManager = plugin.getGameManager();
        this.voidY = this.gameManager.getArena().getVoidY();
    }

    @EventHandler
    public void moveEvent(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.getLocation().getY() <= this.voidY)
            this.gameManager.slain(player);
    }
}
