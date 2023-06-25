package nl.scoutcraft.pillowfight.data;

import nl.scoutcraft.eagle.api.Eagle;
import nl.scoutcraft.pillowfight.game.GameManager;
import nl.scoutcraft.pillowfight.game.wrapper.PillowPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Metrics {

    private static final Logger LOGGER = Logger.getLogger("PillowFight-Metrics");

    public static void saveMetrics(GameManager gameManager) {
        String gameId = UUID.randomUUID().toString();
        try (Connection conn = Eagle.getSQLManager().getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO player VALUES (?, 0) ON DUPLICATE KEY UPDATE Karma = Karma;");
             PreparedStatement gamePs = conn.prepareStatement("INSERT INTO pf_game VALUES (?, ?, ?, ?, ?, ?, ?)");
             PreparedStatement playerPs = conn.prepareStatement("INSERT INTO pf_gameplayer VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            gamePs.setString(1, gameId);
            gamePs.setString(2, gameManager.getArena().getName());
            gamePs.setString(3, gameManager.getTeamsMode().getName());
            gamePs.setBoolean(4, gameManager.getPowerupSpawner() != null);
            gamePs.setInt(5, gameManager.getGamePlayers());
            gamePs.setObject(6, gameManager.getStartTime());
            gamePs.setObject(7, gameManager.getEndTime());
            gamePs.execute();

            for (PillowPlayer pp : PillowPlayer.values()) {
                ps.setString(1, pp.getUuid().toString());
                ps.execute();

                playerPs.setString(1, pp.getUuid().toString());
                playerPs.setString(2, gameId);
                playerPs.setInt(3, pp.getKills());
                playerPs.setInt(4, pp.getDeaths());
                playerPs.setInt(5, pp.getSlaps());
                playerPs.setInt(6, pp.getHits());
                playerPs.setInt(7, pp.getTimePlayed());
                playerPs.setInt(8, pp.getPowerupPickups());
                playerPs.setInt(9, pp.getPowerupUses());
                playerPs.setInt(10, pp.getPlace());
                playerPs.execute();
            }
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to save game", exc);
        }
    }

    public static void addKarma(UUID uuid, int karma) {
        try (Connection conn = Eagle.getSQLManager().getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE player SET Karma = Karma + ? WHERE Uuid = ?")) {
            ps.setInt(1, karma);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException exc) {
            LOGGER.log(Level.SEVERE, "Failed to give karma to player!", exc);
        }
    }
}
