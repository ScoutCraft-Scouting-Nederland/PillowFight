package nl.scoutcraft.pillowfight.game;

import nl.scoutcraft.pillowfight.game.team.ITeamFiller;
import nl.scoutcraft.pillowfight.game.team.TeamFiller1v1;
import nl.scoutcraft.pillowfight.game.team.TeamFiller2v2;
import nl.scoutcraft.pillowfight.game.team.TeamFiller4v4;
import org.bukkit.Material;

public enum TeamsMode {

    SOLOS("solo", "Solo", Material.SUNFLOWER, 2, 3, new TeamFiller1v1()),
    DUOS("duos", "Duos", Material.SLIME_BALL, 4, 6, new TeamFiller2v2()),
    TEAMS("teams", "Teams", Material.CROSSBOW, 6, 12, new TeamFiller4v4());

    private final String id;
    private final String name;
    private final Material material;
    private final int minPlayers;
    private final int maxHearts;
    private final ITeamFiller teamFiller;

    TeamsMode(String id, String name, Material material, int minPlayers, int maxHearts, ITeamFiller teamFiller) {
        this.id = id;
        this.name = name;
        this.material = material;
        this.minPlayers = minPlayers;
        this.maxHearts = maxHearts;
        this.teamFiller = teamFiller;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public Material getMaterial() {
        return this.material;
    }

    public int getMinPlayers() {
        return this.minPlayers;
    }

    public int getMaxHearts() {
        return this.maxHearts;
    }

    public ITeamFiller getTeamFiller() {
        return this.teamFiller;
    }

    public static TeamsMode of(String id) {
        switch (id.toLowerCase()) {
            case "duos": return DUOS;
            case "teams": return TEAMS;
            default: return SOLOS;
        }
    }
}
