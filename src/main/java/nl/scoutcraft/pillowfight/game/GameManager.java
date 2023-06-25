package nl.scoutcraft.pillowfight.game;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import nl.scoutcraft.eagle.api.Eagle;
import nl.scoutcraft.eagle.api.locale.IMessage;
import nl.scoutcraft.eagle.api.locale.MessagePlaceholder;
import nl.scoutcraft.eagle.api.locale.Placeholder;
import nl.scoutcraft.eagle.api.utils.Countdown;
import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.data.Item;
import nl.scoutcraft.pillowfight.data.Metrics;
import nl.scoutcraft.pillowfight.game.powerup.Powerups;
import nl.scoutcraft.pillowfight.game.runnables.countdown.LobbyCountdown;
import nl.scoutcraft.pillowfight.game.runnables.countdown.PowerupCountdown;
import nl.scoutcraft.pillowfight.game.runnables.timer.GameTimer;
import nl.scoutcraft.pillowfight.game.runnables.timer.PowerupSpawner;
import nl.scoutcraft.pillowfight.game.team.Team;
import nl.scoutcraft.pillowfight.game.voting.VotingMenu;
import nl.scoutcraft.pillowfight.game.wrapper.PillowPlayer;
import nl.scoutcraft.pillowfight.lang.Locale;
import nl.scoutcraft.pillowfight.listener.DamageListener;
import nl.scoutcraft.pillowfight.listener.DoubleJumpListener;
import nl.scoutcraft.pillowfight.listener.GGListener;
import nl.scoutcraft.pillowfight.listener.MoveListener;
import nl.scoutcraft.pillowfight.manager.ScoreboardManager;
import nl.scoutcraft.pillowfight.utils.BungeeUtil;
import nl.scoutcraft.pillowfight.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Level;

public class GameManager {

    private static final ChatColor GREEN = ChatColor.of("#81C14B");
    private static final ChatColor RED = ChatColor.of("#D72638");

    public static  final int MIN_PLAYERS = PillowFight.getInstance().getConfig().getInt("MIN_PLAYERS");
    public static final int MAX_PLAYERS = PillowFight.getInstance().getConfig().getInt("MAX_PLAYERS");
    private static final int POWERUP_INTERVAL = PillowFight.getInstance().getConfig().getInt("POWERUP_INTERVAL");
    private static final int RESPAWN_INVULNERABLE_SECONDS = PillowFight.getInstance().getConfig().getInt("RESPAWN_INVULNERABLE_SECONDS");

    private final PillowFight plugin;
    private final List<Player> players;
    private final List<Player> spectators;
    private final List<Team> teams;
    private final Map<Player, PowerupCountdown> powerups;
    private final VotingMenu votingMenu;
    private final Location lobbySpawn;

    private boolean gameStarted = false;
    private boolean gameEnded = false;
    private int gamePlayers;

    @Nullable private TeamsMode teamsMode = null;
    @Nullable private LobbyCountdown lobbyCountdown = null;
    @Nullable private GameTimer gameTimer = null;
    @Nullable private PowerupSpawner powerupSpawner = null;
    @Nullable private Arena arena = null;
    @Nullable private Location spawn = null;
    @Nullable private LocalDateTime startTime = null;
    @Nullable private LocalDateTime endTime = null;

    public GameManager(PillowFight plugin) {
        this.plugin = plugin;

        this.players = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.powerups = new HashMap<>();
        this.votingMenu = new VotingMenu(this.plugin.getArenaManager().getRandomArenas(3));
        this.lobbySpawn = new Location(Bukkit.getWorld(
                plugin.getConfig().getString("LOBBY.world")),
                plugin.getConfig().getDouble("LOBBY.x"),
                plugin.getConfig().getDouble("LOBBY.y"),
                plugin.getConfig().getDouble("LOBBY.z"),
                (float) plugin.getConfig().getDouble("LOBBY.yaw"),
                (float) plugin.getConfig().getDouble("LOBBY.pitch"));
    }

    public void join(Player player) {
        player.setAllowFlight(false);
        player.setFlying(false);
        player.setInvisible(false);
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);
        player.getInventory().clear();
        player.playerListName(Component.text(player.getName(), TextColor.color(Color.OLIVE.asRGB())));

        if (this.gameStarted || this.players.size() >= MAX_PLAYERS) {
            spectate(player);
            return;
        }

        this.spectators.remove(player);
        this.players.add(player);

        if (this.lobbyCountdown == null || this.lobbyCountdown.getCounter() > 10) Item.VOTES.apply(player);
        Item.RULEBOOK.apply(player);
        Item.SPECTATE.apply(player);
        Item.GLOBE.apply(player);

        player.teleport(this.lobbySpawn);
        PlayerUtil.setHealth(player, 6d, false);

        this.startLobbyCountdown(false);

        // Hides all spectators for the player
        this.spectators.forEach(spec -> player.hidePlayer(this.plugin, spec));

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (player != p) p.showPlayer(this.plugin, player);

            Locale.PASS_LOBBY.send(p, new Placeholder("%player%", player.getName()),
                    new Placeholder("%pass%", Locale.JOINED.get(p, true)),
                    new Placeholder("%players%", ((this.getPlayers().size() >= MIN_PLAYERS ? GREEN : RED).toString() + this.getPlayers().size())),
                    new Placeholder("%maxPlayers%", Integer.toString(MAX_PLAYERS)));
        });
    }

    public void quit(Player player) {
        this.plugin.getBoardManager().removeSpectator(player, false);
        this.players.remove(player);

        if (this.gameStarted) {
            this.killed(player);
            return;
        }

        this.votingMenu.removeVotes(player.getUniqueId());

        Bukkit.getOnlinePlayers().forEach(p -> Locale.PASS_LOBBY
                .send(p, new Placeholder("%player%", player.getName()),
                        new Placeholder("%pass%", Locale.LEFT.get(p, true)),
                        new Placeholder("%players%", ((this.getPlayers().size() >= MIN_PLAYERS ? GREEN : RED).toString() + this.getPlayers().size())),
                        new Placeholder("%maxPlayers%", Integer.toString(MAX_PLAYERS))));
    }

    public void spectate(Player player) {
        this.plugin.getBoardManager().addSpectator(this.plugin, player);
        this.spectators.add(player);

        player.teleport(this.gameStarted && this.spawn != null ? this.spawn : this.lobbySpawn);
        player.playerListName(Component.text(player.getName(), TextColor.color(Color.GRAY.asRGB())));
        player.getInventory().clear();
        player.setInvisible(true);
        player.setGameMode(org.bukkit.GameMode.ADVENTURE);
        player.setAllowFlight(true);
        player.setFlying(true);
        PlayerUtil.setHealth(player, 20d, true);

        if (!this.gameStarted) {
            Item.RULEBOOK.apply(player);
            Item.JOIN.apply(player);
        }
        Item.GLOBE.apply(player);

        // Hides the spectator for all ingame players.
        this.players.forEach(inGame -> inGame.hidePlayer(this.plugin, player));
    }

    public void startLobbyCountdown(boolean force) {
        if (!force && this.players.size() < MIN_PLAYERS)
            return;

        if (this.lobbyCountdown == null)
            (this.lobbyCountdown = new LobbyCountdown(this.plugin)).start();

        if (force && this.lobbyCountdown.getCounter() > 10)
            this.lobbyCountdown.setCounter(10);
    }

    public void startGame() {
        if (this.arena == null || this.teamsMode == null || this.players.size() < 2 || (this.spawn = this.arena.getSpawn().getLocation()).getWorld() == null) {
            this.plugin.getLogger().log(Level.SEVERE, "Something went wrong trying to start the game!", new IllegalStateException());
            Bukkit.shutdown();
            return;
        }

        this.gameStarted = true;
        this.gameTimer = new GameTimer(plugin);
        this.gameTimer.start();
        this.startTime = LocalDateTime.now();
        this.gamePlayers = this.players.size();

        Collections.shuffle(this.players);
        this.teamsMode.getTeamFiller().fillTeams(this.teams, this.players);

        Bukkit.getOnlinePlayers().forEach(p -> p.teleport(this.spawn));

        ScoreboardManager boards = this.plugin.getBoardManager();
        for (Player player : this.players) {
            Scoreboard board = boards.getBoard(player);
            player.setScoreboard(board);
            boards.update(board, ScoreboardManager.ARENA_NAME, this.arena);
            boards.update(board, ScoreboardManager.TIME, this.gameTimer.seconds);
            boards.update(board, ScoreboardManager.PLAYER_COUNT, this.gamePlayers);

            Powerups.INVINCIBLE.use(player, RESPAWN_INVULNERABLE_SECONDS, true);
            Locale.TITLE_SUCCES.sendTitle(player, Locale.SUBTITLE_SUCCES, 10, 60, 10);
        }

        for (Team team : this.teams) {
            int health = this.teamsMode.getMaxHearts() / team.getPlayers().size() * 2;

            for (Player player : team.getPlayers()) {
                PlayerUtil.setHealth(player, health, false);

                if (team.getArmorColor() != null)
                    Item.giveArmor(player, team.getArmorColor());
            }
        }

        if (this.powerupSpawner != null)
            this.powerupSpawner.start();

        for (Player sp : this.spectators) {
            sp.getInventory().clear();
            Item.GLOBE.apply(sp);
        }

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new DamageListener(this.plugin), this.plugin);
        pm.registerEvents(new MoveListener(this.plugin), this.plugin);
        pm.registerEvents(new DoubleJumpListener(), this.plugin);
    }

    public void slain(Player player) {
        if (this.gameEnded) return;

        if (!this.players.contains(player)) {
            player.teleport(this.spawn != null ? this.spawn : this.lobbySpawn);
            return;
        }

        Player killer = player.getKiller();

        PillowPlayer.get(player).addDeath();
        if (killer != null) PillowPlayer.get(killer).addKill();

        this.removeTask(player);

        double absorption = player.getAbsorptionAmount();
        double newHealth = player.getHealth();

        if (absorption >= 2D) {
            player.setAbsorptionAmount(absorption - 2D);
        } else {
            newHealth -= 2D;
        }

        if (newHealth > 0) {
            Powerups.INVINCIBLE.use(player, RESPAWN_INVULNERABLE_SECONDS, true);
            player.setHealth(newHealth);
        } else {
            Locale.TITLE_DEFEAT.sendTitle(player, Locale.SUBTITLE_DEFEAT, 10, 60, 10, (killer != null) ? new Placeholder("%killer%", killer.getName()) : new MessagePlaceholder("%killer%", Locale.VOID));
            this.spectate(player);
            this.killed(player);
        }

        player.getWorld().strikeLightningEffect(player.getLocation());

        IMessage<String> message = newHealth > 0 ? Locale.LOST_LIVE : Locale.CHAT_DEFEAT;
        Bukkit.getOnlinePlayers().forEach(p -> message.send(p, new Placeholder("%player%", player.getName()), new Placeholder("%killer%", killer != null ? killer.getName() : Locale.VOID.get(p, true))));

        player.teleport(this.spawn);
    }

    private void killed(Player player) {
        if (!this.gameStarted || this.gameEnded) return;

        this.players.remove(player);
        this.players.forEach(p -> this.plugin.getBoardManager().update(p, ScoreboardManager.PLAYER_COUNT, this.players.size()));

        PillowPlayer.get(player).setTimePlayed(this.gameTimer.seconds);

        this.teams.stream().filter(team -> team.kill(player) && team.isDead()).findFirst().ifPresent(team -> {
            team.getOriginalPlayers().forEach(uuid -> PillowPlayer.get(uuid).setPlace(this.teams.size()));
            this.teams.remove(team);
        });

        if (this.teams.size() == 1) {
            this.endGame(this.teams.get(0));
        } else if (this.teams.size() == 0) {
            this.plugin.getLogger().log(Level.SEVERE, "This code is unreachable. Report this to the developers!", new IllegalStateException());
            Bukkit.shutdown();
        }
    }

    public void addTask(Player player, PowerupCountdown task) {
        this.removeTask(player);
        this.powerups.put(player, task);
    }

    public void removeTask(Player player) {
        Optional.ofNullable(this.powerups.remove(player)).ifPresent(c -> { c.stop(); c.onZero(); });
    }

    public void endGame(Team winnerTeam) {
        if (this.gameEnded) return;

        this.gameEnded = true;
        this.gameTimer.cancel();
        this.endTime = LocalDateTime.now();
        this.powerups.values().forEach(Countdown::stop);
        if (this.powerupSpawner != null) this.powerupSpawner.stop();

        this.players.forEach(p -> { PillowPlayer.get(p).setTimePlayed(this.gameTimer.seconds); this.spectate(p); });
        this.players.clear();
        winnerTeam.getOriginalPlayers().forEach(uuid -> PillowPlayer.get(uuid).setPlace(1));

        if (winnerTeam.getPlayers().isEmpty()) {
            Bukkit.getOnlinePlayers().forEach(p -> Locale.TITLE_DRAW.sendTitle(p, Locale.EMPTY, 10, 100, 10));
            this.plugin.getLogger().log(Level.SEVERE, "This is not possible! WTF happened! Call Daniel right away!", new IllegalStateException());
        } else if (this.teamsMode == TeamsMode.SOLOS) {
            String winnerName = winnerTeam.getPlayers().get(0).getName();
            Bukkit.getOnlinePlayers().forEach(p -> Locale.TITLE_WIN.sendTitle(p, Locale.SUBTITLE_WIN, 10, 100, 10, new Placeholder("%winner%", winnerName)));
        } else {
            Bukkit.getOnlinePlayers().forEach(p -> Locale.TITLE_TEAM_WIN.sendTitle(p, Locale.SUBTITLE_WIN, 10, 100, 10, new MessagePlaceholder("%color%", winnerTeam.getName())));
        }

        Metrics.saveMetrics(this);

        Bukkit.getPluginManager().registerEvents(new GGListener(this.plugin), this.plugin);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> Bukkit.getOnlinePlayers().forEach(BungeeUtil::sendToLobby), 200L);
        Bukkit.getScheduler().runTaskLater(this.plugin, () -> Bukkit.getServer().shutdown(), 220L);
    }

    public void applyVoteChoices() {
        this.arena = this.plugin.getArenaManager().load(this.votingMenu.chooseArena());
        this.teamsMode = this.votingMenu.chooseMode();
        this.powerupSpawner = this.votingMenu.choosePowerups() ? new PowerupSpawner(this.plugin, this.arena.getPowerupArea(), POWERUP_INTERVAL) : null;

        if (this.players.size() < this.teamsMode.getMinPlayers())
            this.teamsMode = TeamsMode.SOLOS;

        this.votingMenu.closeAll();

        Eagle.getMapManager().loadMap(this.arena);
    }

    public int getGamePlayers() {
        return this.gamePlayers;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public VotingMenu getVotingMenu() {
        return this.votingMenu;
    }

    public boolean isGameStarted() {
        return this.gameStarted;
    }

    @Nullable
    public TeamsMode getTeamsMode() {
        return this.teamsMode;
    }

    @Nullable
    public GameTimer getGameTimer() {
        return this.gameTimer;
    }

    @Nullable
    public PowerupSpawner getPowerupSpawner() {
        return this.powerupSpawner;
    }

    @Nullable
    public Arena getArena() {
        return this.arena;
    }

    @Nullable
    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    @Nullable
    public LocalDateTime getEndTime() {
        return this.endTime;
    }
}
