package nl.scoutcraft.pillowfight.manager;

import nl.scoutcraft.eagle.api.Eagle;
import nl.scoutcraft.pillowfight.game.Arena;
import nl.scoutcraft.pillowfight.utils.Area;
import nl.scoutcraft.pillowfight.utils.Position;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Random;

public class ArenaManager {

    public List<String> getRandomArenas(int count) {
        List<String> arenas = Eagle.getMapManager().getMapNames("SELECT Arena FROM pf_arenas", rs -> rs.getString("Arena"));

        Random r = new Random();
        while (arenas.size() > count)
            arenas.remove(r.nextInt(arenas.size()));

        return arenas;
    }

    @Nullable
    public Arena load(String name) {
        String sql = String.format("SELECT * FROM pf_arenas WHERE Arena='%s'", name);

        return Eagle.getMapManager().getMap(sql, rs ->
                new Arena(name,
                        rs.getString("SlimeName"),
                        rs.getInt("Time"),
                        Position.deserialize(rs.getString("Spawn")),
                        rs.getInt("VoidY"),
                        Area.deserialize(rs.getString("PowerupArea"))));
    }
}
