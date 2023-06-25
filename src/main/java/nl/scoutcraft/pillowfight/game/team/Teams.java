package nl.scoutcraft.pillowfight.game.team;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.ChatColor;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Color;
import org.bukkit.Material;

import java.util.Collections;
import java.util.List;

public final class Teams {

//    public static final Team LIME = new Team(99, Locale.TEAM_LIME, ChatColor.GREEN, Color.LIME, Material.LIME_WOOL);
    public static final Team YELLOW = new Team(1,Locale.TEAM_YELLOW, ChatColor.YELLOW, Color.YELLOW, Material.YELLOW_WOOL);
    public static final Team AQUA = new Team(2, Locale.TEAM_AQUA, ChatColor.AQUA, Color.AQUA, Material.LIGHT_BLUE_WOOL);
    public static final Team BLUE = new Team(3, Locale.TEAM_BLUE, ChatColor.DARK_BLUE, Color.BLUE, Material.BLUE_WOOL);
    public static final Team PURPLE = new Team(4, Locale.TEAM_PURPLE, ChatColor.DARK_PURPLE, Color.PURPLE, Material.PURPLE_WOOL);
    public static final Team GREEN = new Team(5, Locale.TEAM_GREEN, ChatColor.DARK_GREEN, Color.GREEN, Material.GREEN_WOOL);
    public static final Team RED = new Team(6, Locale.TEAM_RED, ChatColor.DARK_RED, Color.RED, Material.RED_WOOL);
    public static final Team WHITE = new Team(7, Locale.TEAM_WHITE, ChatColor.WHITE, Color.WHITE, Material.WHITE_WOOL);
    public static final Team BROWN = new Team(8, Locale.TEAM_BROWN, ChatColor.of("#795548"), Color.fromRGB(79, 55, 48), Material.BROWN_WOOL);

    public static final Team T01 = new Team(11, null, ChatColor.GREEN, null, Material.WHITE_WOOL);
    public static final Team T02 = new Team(12, null, ChatColor.GREEN, null, Material.ORANGE_WOOL);
    public static final Team T03 = new Team(13, null, ChatColor.GREEN, null, Material.MAGENTA_WOOL);
    public static final Team T04 = new Team(14, null, ChatColor.GREEN, null, Material.LIGHT_BLUE_WOOL);
    public static final Team T05 = new Team(15, null, ChatColor.GREEN, null, Material.YELLOW_WOOL);
    public static final Team T06 = new Team(16, null, ChatColor.GREEN, null, Material.LIME_WOOL);
    public static final Team T07 = new Team(17, null, ChatColor.GREEN, null, Material.PINK_WOOL);
    public static final Team T08 = new Team(18, null, ChatColor.GREEN, null, Material.GRAY_WOOL);
    public static final Team T09 = new Team(19, null, ChatColor.GREEN, null, Material.LIGHT_GRAY_WOOL);
    public static final Team T10 = new Team(20, null, ChatColor.GREEN, null, Material.CYAN_WOOL);
    public static final Team T11 = new Team(21, null, ChatColor.GREEN, null, Material.PURPLE_WOOL);
    public static final Team T12 = new Team(22, null, ChatColor.GREEN, null, Material.BLUE_WOOL);
    public static final Team T13 = new Team(23, null, ChatColor.GREEN, null, Material.BROWN_WOOL);
    public static final Team T14 = new Team(24, null, ChatColor.GREEN, null, Material.GREEN_WOOL);
    public static final Team T15 = new Team(25, null, ChatColor.GREEN, null, Material.RED_WOOL);
    public static final Team T16 = new Team(26, null, ChatColor.GREEN, null, Material.BLACK_WOOL);

    private static final List<Team> TEAMS = Lists.newArrayList(YELLOW, AQUA, BLUE, PURPLE, GREEN, RED, WHITE, BROWN);
    private static final List<Team> SOLO_TEAMS = Lists.newArrayList(T01, T02, T03, T04, T05, T06, T07, T08, T09, T10, T11, T12, T13, T14, T15, T16);

    private static int TEAMS_INDEX = 0;
    private static int SOLO_TEAMS_INDEX = 0;

    public static Team getRandom(boolean solo) {
        return (solo ? SOLO_TEAMS : TEAMS).get(solo ? SOLO_TEAMS_INDEX++ : TEAMS_INDEX++);
    }

    public static Color getColor(int id) {
        if (id < 1 || id > 8)
            return Color.WHITE;

        for (Team team : TEAMS)
            if (id == team.getId())
                return team.getArmorColor();

        return Color.WHITE;
    }

    static {
        Collections.shuffle(TEAMS);
        Collections.shuffle(SOLO_TEAMS);
    }

    private Teams(){}
}
