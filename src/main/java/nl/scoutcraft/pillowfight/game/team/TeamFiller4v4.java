package nl.scoutcraft.pillowfight.game.team;

import org.bukkit.entity.Player;

import java.util.List;

public class TeamFiller4v4 implements ITeamFiller {

    @Override
    public void fillTeams(List<Team> teams, List<Player> players) {
        int playerCount = players.size();
        int teamCount = playerCount / 4 + (playerCount % 4 > 0 ? 1 : 0);

        for (int i = 0; i < teamCount; i++)
            teams.add(Teams.getRandom(false));

        int teamIndex = 0;
        for (Player player : players) {
            teams.get(teamIndex++).addPlayer(player);
            if (teamIndex >= teamCount) teamIndex = 0;
        }
    }
}
