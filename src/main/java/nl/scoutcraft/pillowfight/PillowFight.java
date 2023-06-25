package nl.scoutcraft.pillowfight;

import nl.scoutcraft.eagle.api.locale.Internationalization;
import nl.scoutcraft.pillowfight.commands.AdminCommand;
import nl.scoutcraft.pillowfight.commands.MetricsCommand;
import nl.scoutcraft.pillowfight.game.GameManager;
import nl.scoutcraft.pillowfight.lang.InternationalizationImpl;
import nl.scoutcraft.pillowfight.listener.GenericListener;
import nl.scoutcraft.pillowfight.manager.ArenaManager;
import nl.scoutcraft.pillowfight.manager.ScoreboardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class PillowFight extends JavaPlugin {

    private static PillowFight plugin;

    private Internationalization lang;
    private ArenaManager arenaManager;
    private GameManager gameManager;
    private ScoreboardManager boardManager;

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        super.saveDefaultConfig();

        if (!getConfig().getBoolean("CONFIGURATED")) {
            Bukkit.getPluginManager().disablePlugin(this);
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Please configure PillowFight before using the plugin!");
            return;
        }

        this.lang = new InternationalizationImpl(this);
        this.arenaManager = new ArenaManager();
        this.gameManager = new GameManager(this);
        this.boardManager = new ScoreboardManager();

        getServer().getPluginManager().registerEvents(new GenericListener(this), this);

        getCommand("admin").setExecutor(new AdminCommand(this));
        getCommand("stats").setExecutor(new MetricsCommand(this));
    }

    public Internationalization getLang() {
        return this.lang;
    }

    public ArenaManager getArenaManager() {
        return this.arenaManager;
    }

    public GameManager getGameManager() {
        return this.gameManager;
    }

    public ScoreboardManager getBoardManager() {
        return this.boardManager;
    }

    public static PillowFight getInstance() {
        return plugin;
    }
}
