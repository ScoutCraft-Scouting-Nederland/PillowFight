package nl.scoutcraft.pillowfight.game.runnables.countdown;

import nl.scoutcraft.eagle.api.locale.IMessage;
import nl.scoutcraft.eagle.api.locale.MessagePlaceholder;
import nl.scoutcraft.eagle.api.locale.Placeholder;
import nl.scoutcraft.eagle.api.utils.Countdown;
import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.data.Keys;
import nl.scoutcraft.pillowfight.game.powerup.Powerup;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

public class PowerupCountdown extends Countdown {

    private final PillowFight plugin;
    private final Powerup powerup;
    private final Player player;

    private final IMessage<String> msg;
    private final IMessage<String> end;

    public PowerupCountdown(PillowFight plugin, Powerup powerup, Player player, IMessage<String> msg, IMessage<String> end, int seconds) {
        super(seconds);

        this.plugin = plugin;
        this.powerup = powerup;
        this.player = player;

        this.msg = msg;
        this.end = end;
    }

    public PowerupCountdown start() {
        this.powerup.start(this.player);
        this.player.getPersistentDataContainer().set(Keys.POWERUP_ID, PersistentDataType.BYTE, this.powerup.getId());
        super.start(this.plugin);
        return this;
    }

    @Override
    public void onSecond(int i) {
        this.msg.sendActionBar(this.player, new Placeholder("%seconds%", Integer.toString(super.counter)), new MessagePlaceholder("%secs%", super.counter > 1 ? Locale.SECONDS : Locale.SECOND));
    }

    @Override
    public void onZero() {
        this.end.sendActionBar(this.player);
        this.powerup.stop(this.player);
        this.player.getPersistentDataContainer().remove(Keys.POWERUP_ID);
        this.plugin.getGameManager().removeTask(this.player);
    }
}
