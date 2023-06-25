package nl.scoutcraft.pillowfight.commands;

import nl.scoutcraft.pillowfight.PillowFight;
import nl.scoutcraft.pillowfight.game.GameManager;
import nl.scoutcraft.pillowfight.lang.Locale;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AdminCommand implements CommandExecutor {

    private final PillowFight plugin;

    private final GameManager gameManager;

    public AdminCommand(PillowFight plugin) {
        this.plugin = plugin;
        this.gameManager = plugin.getGameManager();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0)
            return false;

        if (args[0].equalsIgnoreCase("forcestart")) {
            if (!this.gameManager.isGameStarted()) {
                this.gameManager.startLobbyCountdown(true);
                sender.sendMessage(Locale.FORCE_START.get(sender instanceof Player ? ((Player) sender) : null, true));
            } else {
                sender.sendMessage(Locale.NOT_AVAILABLE.get(sender instanceof Player ? ((Player) sender) : null, true));
            }
            return true;
        } else if (args[0].equalsIgnoreCase("setlobbyspawn")) {
            if (!(sender instanceof Player))
                return true;

            Player player = (Player) sender;
            FileConfiguration config = this.plugin.getConfig();
            Location location = player.getLocation();
            config.set("LOBBY.world", location.getWorld().getName());
            config.set("LOBBY.x", location.getX());
            config.set("LOBBY.y", location.getY());
            config.set("LOBBY.z", location.getZ());
            config.set("LOBBY.pitch", location.getPitch());
            config.set("LOBBY.yaw", location.getYaw());
            this.plugin.saveConfig();
            Locale.SPAWN_SET.send(player);
            return true;
        }

        return false;
    }
}
