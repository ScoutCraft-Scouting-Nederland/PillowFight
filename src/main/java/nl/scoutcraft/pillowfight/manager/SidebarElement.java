package nl.scoutcraft.pillowfight.manager;

import nl.scoutcraft.eagle.api.data.EagleKeys;
import nl.scoutcraft.eagle.api.locale.IMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.function.Function;

public class SidebarElement<T> {

    private final String name;
    private final ChatColor color;
    private final int score;
    private final IMessage<String> message;
    private final String defaultValue;
    private final Function<T, String> transformer;

    public SidebarElement(String name, ChatColor color, int score, IMessage<String> message, String defaultValue, Function<T, String> transformer) {
        this.score = score;
        this.name = name;
        this.color = color;
        this.message = message;
        this.defaultValue = defaultValue;
        this.transformer = transformer;
    }

    public SidebarElement<T> apply(Player p, Scoreboard board, Objective objective) {
        return this.apply(p.getPersistentDataContainer().get(EagleKeys.LOCALE, EagleKeys.LOCALE_TAG_TYPE), board, objective);
    }

    public SidebarElement<T> apply(@Nullable Locale locale, Scoreboard board, Objective objective) {
        objective.getScore(this.color.toString()).setScore(this.score);

        if (board.getTeam(this.name) == null) {
            Team team = board.registerNewTeam(this.name);
            team.setPrefix(this.message.get(locale, true));
            team.addEntry(this.color.toString());
        }

        return this;
    }

    public SidebarElement<T> update(Scoreboard board, @Nullable T value) {
        Team team = board.getTeam(this.name);
        if (team != null)
            team.setSuffix(value == null ? this.defaultValue : this.transformer.apply(value));

        return this;
    }
}
