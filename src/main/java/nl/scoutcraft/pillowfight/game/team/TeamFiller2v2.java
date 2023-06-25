package nl.scoutcraft.pillowfight.game.team;

import org.bukkit.entity.Player;

import java.util.List;

public class TeamFiller2v2 implements ITeamFiller {

    @Override
    public void fillTeams(List<Team> teams, List<Player> players) {
        Team team = null;
        for (Player player : players) {
            if (team == null || team.getPlayers().size() >= 2) {
                team = Teams.getRandom(false);
                teams.add(team);
            }

            team.addPlayer(player);
        }
    }
}
