package nl.scoutcraft.pillowfight.game.team;

import org.bukkit.entity.Player;

import java.util.List;

public interface ITeamFiller {

    void fillTeams(List<Team> teams, List<Player> players);
}
