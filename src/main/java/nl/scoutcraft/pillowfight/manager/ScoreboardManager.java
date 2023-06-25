package nl.scoutcraft.pillowfight.manager;

import nl.scoutcraft.eagle.api.locale.BlankMessage;
import nl.scoutcraft.eagle.api.locale.IMessage;
import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.game.Arena;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager {

    private static final IMessage<String> BLANK = new BlankMessage();

    public static final SidebarElement<String> SPACER_1 = new SidebarElement<>("spacer_1", ChatColor.DARK_GRAY, 10, BLANK, "", v -> "");
    public static final SidebarElement<String> ARENA = new SidebarElement<>("arena", ChatColor.BLACK, 9, Locale.SCOREBOARD_ARENA, "", v -> "");
    public static final SidebarElement<Arena> ARENA_NAME = new SidebarElement<>("arenaName", ChatColor.BLUE, 8, Locale.SCOREBOARD_ARENA_NAME, "", Arena::getName);
    public static final SidebarElement<String> SPACER_2 = new SidebarElement<>("spacer_2", ChatColor.DARK_GREEN, 7, BLANK, "", v -> "");
    public static final SidebarElement<String> PLAY_TIME = new SidebarElement<>("gameTime", ChatColor.GRAY, 6, Locale.SCOREBOARD_PLAY_TIME, "", v -> "");
    public static final SidebarElement<Integer> TIME = new SidebarElement<>("gameTimer", ChatColor.LIGHT_PURPLE, 5, BLANK, "", ScoreboardManager::secToMin);
    public static final SidebarElement<String> SPACER_3 = new SidebarElement<>("spacer_3", ChatColor.DARK_AQUA, 4, BLANK, "", v -> "");
    public static final SidebarElement<String> PLAYERS = new SidebarElement<>("players", ChatColor.DARK_PURPLE, 3, Locale.SCOREBOARD_PLAYERS, "", v -> "");
    public static final SidebarElement<Integer> PLAYER_COUNT = new SidebarElement<>("playerCount", ChatColor.GOLD, 2, BLANK, "0", ScoreboardManager::playerCount);
    public static final SidebarElement<String> SPACER_4 = new SidebarElement<>("spacer_4", ChatColor.DARK_RED, 1, BLANK, "", v -> "");
    public static final SidebarElement<String> URL = new SidebarElement<>("scoutCraft", ChatColor.DARK_BLUE, 0, Locale.SCOREBOARD_URL, "", v -> "");

    private final Map<UUID, Scoreboard> boards;

    private final Scoreboard spectatorBoard;
    private final org.bukkit.scoreboard.Team spectatorTeam;

    public ScoreboardManager() {
        this.boards = new HashMap<>();
        this.spectatorBoard = Bukkit.getScoreboardManager().getNewScoreboard();
        this.spectatorTeam = this.spectatorBoard.registerNewTeam("spectate");
        this.spectatorTeam.setOption(org.bukkit.scoreboard.Team.Option.NAME_TAG_VISIBILITY, org.bukkit.scoreboard.Team.OptionStatus.NEVER);
    }

    private static String secToMin(int i) {
        int ms = i / 60;
        int ss = i % 60;
        String m = ((ms < 10) ? "0" : "") + ms;
        String s = ((ss < 10) ? "0" : "") + ss;
        return Locale.SCOREBOARD_TIME.get((java.util.Locale) null, true).replace("%minutes%", m).replace("%seconds%", s);
    }

    private static String playerCount(int players) {
        return Locale.SCOREBOARD_PLAYER_COUNT.get((java.util.Locale) null, true).replace("%alivePlayers%", Integer.toString(players)).replace("%gamePlayers%", Integer.toString(PillowFight.getInstance().getGameManager().getGamePlayers()));
    }

    public <T> void update(Player player, SidebarElement<T> element, @Nullable T value) {
        this.update(this.getBoard(player), element, value);
    }

    public <T> void update(Scoreboard board, SidebarElement<T> element, @Nullable T value) {
        element.update(board, value);
    }

    public void addSpectator(PillowFight plugin, Player player) {
        player.setScoreboard(this.spectatorBoard);
        this.spectatorTeam.addEntry(player.getName());

        // Show everyone to the spectator
        Bukkit.getOnlinePlayers().forEach(p -> player.showPlayer(plugin, p));
    }

    public void removeSpectator(Player player, boolean setGameBoard) {
        this.spectatorTeam.removeEntry(player.getName());
        if (setGameBoard)
            player.setScoreboard(this.getBoard(player));
    }

    public Scoreboard getBoard(Player player) {
        return this.boards.computeIfAbsent(player.getUniqueId(), k -> this.create(player));
    }

    private Scoreboard create(Player player) {
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();

        Objective obj = board.getObjective("sidebar");
        if (obj == null) {
            obj = board.registerNewObjective("sidebar", "dummy", Locale.SCOREBOARD_TITLE.get(player, true), RenderType.INTEGER);
            obj.setDisplaySlot(DisplaySlot.SIDEBAR);
        }

        SPACER_1.apply(player, board, obj).update(board, null);
        ARENA.apply(player, board, obj).update(board, null);
        ARENA_NAME.apply(player, board, obj).update(board, null);
        SPACER_2.apply(player, board, obj).update(board, null);
        PLAY_TIME.apply(player, board, obj).update(board, null);
        TIME.apply(player, board, obj).update(board, null);
        SPACER_3.apply(player, board, obj).update(board, null);
        PLAYERS.apply(player, board, obj).update(board, null);
        PLAYER_COUNT.apply(player, board, obj).update(board, null);
        SPACER_4.apply(player, board, obj).update(board, null);
        URL.apply(player, board, obj).update(board, null);

        return board;
    }
}
