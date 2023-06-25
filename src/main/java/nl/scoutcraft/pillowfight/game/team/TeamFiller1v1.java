package nl.scoutcraft.pillowfight.game.team;

import org.bukkit.entity.Player;

import java.util.List;

public class TeamFiller1v1 implements ITeamFiller {

    @Override
    public void fillTeams(List<Team> teams, List<Player> players) {
        players.forEach(p -> teams.add(Teams.getRandom(true).addPlayer(p)));
    }
}
