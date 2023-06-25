package nl.scoutcraft.pillowfight.commands;

import nl.scoutcraft.eagle.api.Eagle;
import nl.scoutcraft.eagle.api.locale.Placeholder;
import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.logging.Level;

public class MetricsCommand implements CommandExecutor {

    private final PillowFight plugin;

    public MetricsCommand(PillowFight plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> this.sendMetrics(player));
        }
        return true;
    }

    private void sendMetrics(Player player) {
        try (Connection conn = Eagle.getSQLManager().getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT * FROM `pf_stats` WHERE Uuid = ?")) {
            ps.setString(1, player.getUniqueId().toString());

            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                Locale.METRICS_NOT_FOUND.send(player);
                return;
            }

            Locale.METRICS_HEADER.send(player);
            Locale.METRICS_GAMES.send(player, new Placeholder("%games%", Integer.toString(rs.getInt("Games"))));
            Locale.METRICS_WINS.send(player, new Placeholder("%wins%", Integer.toString(rs.getInt("Wins"))));
            Locale.METRICS_WINRATIO.send(player, new Placeholder("%winratio%", rs.getString("WinRatio")));
            Locale.METRICS_KILLS.send(player, new Placeholder("%kills%", Integer.toString(rs.getInt("Kills"))));
            Locale.METRICS_DEATHS.send(player, new Placeholder("%deaths%", Integer.toString(rs.getInt("Deaths"))));
            Locale.METRICS_KD.send(player, new Placeholder("%kd%", Double.toString(rs.getDouble("KD"))));
            Locale.METRICS_SLAPS.send(player, new Placeholder("%slaps%", Integer.toString(rs.getInt("Slaps"))));
            Locale.METRICS_HITS.send(player, new Placeholder("%hits%", Integer.toString(rs.getInt("Hits"))));
            Locale.METRICS_KARMA.send(player, new Placeholder("%karma%", Integer.toString(rs.getInt("Karma"))));

        } catch (SQLException exc) {
            this.plugin.getLogger().log(Level.SEVERE, "Failed to get player metrics", exc);
        }
    }
}
