package nl.scoutcraft.pillowfight.game.runnables.countdown;

import nl.scoutcraft.eagle.api.locale.IPlaceholder;
import nl.scoutcraft.eagle.api.locale.MessagePlaceholder;
import nl.scoutcraft.eagle.api.locale.Placeholder;
import nl.scoutcraft.eagle.api.utils.Countdown;
import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.data.Item;
import nl.scoutcraft.pillowfight.game.GameManager;
import nl.scoutcraft.pillowfight.lang.Locale;

public class LobbyCountdown extends Countdown {

    private final PillowFight plugin;
    private final GameManager gameManager;

    public LobbyCountdown(PillowFight plugin) {
        super(60);

        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    public void start() {
        super.start(this.plugin);
    }

    @Override
    public void onSecond(int seconds) {
        if (seconds == 10) {
            this.gameManager.applyVoteChoices();
            this.gameManager.getPlayers().forEach(p -> { p.getInventory().clear(); Item.GLOBE.apply(p); });
        } else if (seconds == 7) {
            IPlaceholder arena = new Placeholder("%arena%", this.gameManager.getArena().getName());
            IPlaceholder teams = new Placeholder("%teams%", this.gameManager.getTeamsMode().getName());
            IPlaceholder mode = new Placeholder("%mode%", this.gameManager.getPowerupSpawner() == null ? "Classic" : "Power-ups");
            this.gameManager.getPlayers().forEach(p -> Locale.TITLE_ARENA.sendTitle(p, Locale.SUBTITLE_ARENA, 10, 100, 10, arena, teams, mode));
        }

        IPlaceholder secsFormat = new MessagePlaceholder("%secs%", seconds > 1 ? Locale.SECONDS : Locale.SECOND);
        IPlaceholder secondsPlaceholder = new Placeholder("%seconds%", Integer.toString(seconds));
        this.gameManager.getPlayers().forEach(p -> Locale.COUNTDOWN.sendActionBar(p, secondsPlaceholder, secsFormat));
    }

    @Override
    public void onZero() {
        this.gameManager.startGame();
        this.stop();
    }
}
