package nl.scoutcraft.pillowfight.lang;

import nl.scoutcraft.eagle.api.locale.*;
import nl.scoutcraft.pillowfight.PillowFight;

public final class Locale {

    private static final Internationalization LANG = PillowFight.getInstance().getLang();

    public static final IMessage<String> EMPTY = new BlankMessage();
    public static final IMessage<String> PREFIX = new Message(LANG, "prefix");
    public static final IMessage<String> CHAT_DEFEAT = new CompoundMessage(LANG, "chat.defeat", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage<String> COUNTDOWN = new Message(LANG, "countdown");
    public static final IMessage<String> FORCE_START = new CompoundMessage(LANG, "forceStart", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage<String> JOINED = new Message(LANG, "joined");
    public static final IMessage<String> LEFT = new Message(LANG, "left");
    public static final IMessage<String> LOST_LIVE = new CompoundMessage(LANG, "lostLive", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage<String> NO_PERMISSION = new CompoundMessage(LANG, "noPermission", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage<String> NOT_AVAILABLE = new CompoundMessage(LANG, "notAvailable", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage<String> PASS_LOBBY = new CompoundMessage(LANG, "passLobby", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage<String> SECOND = new Message(LANG, "second");
    public static final IMessage<String> SECONDS = new Message(LANG, "seconds");
    public static final IMessage<String> VOID = new Message(LANG, "void");
    public static final IMessage<String> SPAWN_SET = new CompoundMessage(LANG, "spawnSet", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage<String> TITLE_ARENA = new Message(LANG, "title.arena");
    public static final IMessage<String> SUBTITLE_ARENA = new Message(LANG, "subtitle.arena");
    public static final IMessage<String> TITLE_DEFEAT = new Message(LANG, "title.defeat");
    public static final IMessage<String> SUBTITLE_DEFEAT = new Message(LANG, "subtitle.defeat");
    public static final IMessage<String> TITLE_SUCCES = new Message(LANG, "title.succes");
    public static final IMessage<String> SUBTITLE_SUCCES = new Message(LANG, "subtitle.succes");
    public static final IMessage<String> TITLE_TEAM_WIN = new Message(LANG, "title.team_win");
    public static final IMessage<String> TITLE_WIN = new Message(LANG, "title.win");
    public static final IMessage<String> SUBTITLE_WIN = new Message(LANG, "subtitle.win");
    public static final IMessage<String> TITLE_DRAW = new Message(LANG, "title.draw");
    public static final IMessage<String> KARMA = new Message(LANG, "karma");

    // Teams
    public static final IMessage<String> TEAM_YELLOW = new Message(LANG, "team.yellow");
    public static final IMessage<String> TEAM_AQUA = new Message(LANG, "team.aqua");
    public static final IMessage<String> TEAM_BLUE = new Message(LANG, "team.blue");
    public static final IMessage<String> TEAM_PURPLE = new Message(LANG, "team.purple");
    public static final IMessage<String> TEAM_GREEN = new Message(LANG, "team.green");
    public static final IMessage<String> TEAM_RED = new Message(LANG, "team.red");
    public static final IMessage<String> TEAM_BROWN = new Message(LANG, "team.brown");
    public static final IMessage<String> TEAM_WHITE = new Message(LANG, "team.white");

    // Buttons
    public static final IMessage<String> BUTTON_VOTES_NAME = new Message(LANG, "button.votes.name");
    public static final IMessage<String> BUTTON_VOTES_LORE = new Message(LANG, "button.votes.lore");
    public static final IMessage<String> BUTTON_RULEBOOK_NAME = new Message(LANG, "button.rulebook.name");
    public static final IMessage<String> BUTTON_RULEBOOK_TITLE = new Message(LANG, "button.rulebook.title");
    public static final IMessage<String> BUTTON_RULEBOOK_AUTHOR = new Message(LANG, "button.rulebook.author");
    public static final IMessage<String> BUTTON_SPECTATE_NAME = new Message(LANG, "button.spectate.name");
    public static final IMessage<String> BUTTON_JOIN_NAME = new Message(LANG, "button.join.name");

    public static final IMessage<String> BUTTON_GLOBE_LORE = new Message(LANG, "button.globe.lore");
    public static final IMessage<String> BUTTON_GLOBE_NAME = new Message(LANG, "button.globe.name");
    public static final IMessage<String> BUTTON_PILLOW_NAME = new Message(LANG, "button.pillow.name");

    // Menu's
    public static final IMessage<String> ITEM_ARENA_NAME = new Message(LANG, "item.arena.name");
    public static final IMessage<String> ITEM_TEAMS_NAME = new Message(LANG, "item.teams.name");
    public static final IMessage<String> ITEM_GAMEMODE_NAME = new Message(LANG, "item.gamemode.name");

    // VOTING
    public static final IMessage<String> VOTE_ARENA = new CompoundMessage(LANG, "vote.arena", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage<String> VOTE_TEAMS = new CompoundMessage(LANG, "vote.teams", new MessagePlaceholder("%prefix%", PREFIX));
    public static final IMessage<String> VOTE_MODE = new CompoundMessage(LANG, "vote.mode", new MessagePlaceholder("%prefix%", PREFIX));

    // Sidebar
    public static final IMessage<String> SCOREBOARD_ARENA = new Message(LANG, "scoreboard.arena");
    public static final IMessage<String> SCOREBOARD_ARENA_NAME = new Message(LANG, "scoreboard.arenaName");
    public static final IMessage<String> SCOREBOARD_PLAYER_COUNT = new Message(LANG, "scoreboard.playerCount");
    public static final IMessage<String> SCOREBOARD_PLAYERS = new Message(LANG, "scoreboard.players");
    public static final IMessage<String> SCOREBOARD_PLAY_TIME = new Message(LANG, "scoreboard.playTime");
    public static final IMessage<String> SCOREBOARD_TIME = new Message(LANG, "scoreboard.time");
    public static final IMessage<String> SCOREBOARD_TITLE = new Message(LANG, "scoreboard.title");
    public static final IMessage<String> SCOREBOARD_URL = new Message(LANG, "scoreboard.url");

    // Powerups
    public static final IMessage<String> POWERUP_INVALID = new Message(LANG, "powerup.not_allowed");
    public static final IMessage<String> DASH_NAME = new Message(LANG, "powerup.dash.name");
    public static final IMessage<String> DASH_LORE = new Message(LANG, "powerup.dash.lore");
    public static final IMessage<String> DOUBLE_JUMP_NAME = new Message(LANG, "powerup.doublejump.name");
    public static final IMessage<String> DOUBLE_JUMP_LORE = new Message(LANG, "powerup.doublejump.lore");
    public static final IMessage<String> DOUBLE_JUMP_COUNTDOWN = new Message(LANG, "powerup.doublejump.countdown");
    public static final IMessage<String> DOUBLE_JUMP_END = new Message(LANG, "powerup.doublejump.end");
    public static final IMessage<String> EXTRA_LIFE_NAME = new Message(LANG, "powerup.extralife.name");
    public static final IMessage<String> EXTRA_LIFE_LORE = new Message(LANG, "powerup.extralife.lore");
    public static final IMessage<String> INVINCIBLE_NAME = new Message(LANG, "powerup.invincible.name");
    public static final IMessage<String> INVINCIBLE_LORE = new Message(LANG, "powerup.invincible.lore");
    public static final IMessage<String> INVINCIBLE_COUNTDOWN = new Message(LANG, "powerup.invincible.countdown");
    public static final IMessage<String> INVINCIBLE_END = new Message(LANG, "powerup.invincible.end");
    public static final IMessage<String> PILLOW_EXPLOSION_NAME = new Message(LANG, "powerup.pillowexplosion.name");
    public static final IMessage<String> PILLOW_EXPLOSION_LORE = new Message(LANG, "powerup.pillowexplosion.lore");
    public static final IMessage<String> SUPER_PILLOW_NAME = new Message(LANG, "powerup.superpillow.name");
    public static final IMessage<String> SUPER_PILLOW_LORE = new Message(LANG, "powerup.superpillow.lore");
    public static final IMessage<String> SUPER_PILLOW_COUNTDOWN = new Message(LANG, "powerup.superpillow.countdown");
    public static final IMessage<String> SUPER_PILLOW_END = new Message(LANG, "powerup.superpillow.end");

    // Metrics
    public static final IMessage<String> METRICS_NOT_FOUND = new Message(LANG, "metrics.not_found");
    public static final IMessage<String> METRICS_HEADER = new Message(LANG, "metrics.header");
    public static final IMessage<String> METRICS_GAMES = new Message(LANG, "metrics.games");
    public static final IMessage<String> METRICS_WINS = new Message(LANG, "metrics.wins");
    public static final IMessage<String> METRICS_WINRATIO = new Message(LANG, "metrics.winratio");
    public static final IMessage<String> METRICS_KILLS = new Message(LANG, "metrics.kills");
    public static final IMessage<String> METRICS_DEATHS = new Message(LANG, "metrics.deaths");
    public static final IMessage<String> METRICS_KD = new Message(LANG, "metrics.kd");
    public static final IMessage<String> METRICS_SLAPS = new Message(LANG, "metrics.slaps");
    public static final IMessage<String> METRICS_HITS = new Message(LANG, "metrics.hits");
    public static final IMessage<String> METRICS_KARMA = new Message(LANG, "metrics.karma");

    private Locale(){}
}
