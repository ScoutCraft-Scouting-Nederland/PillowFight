package nl.scoutcraft.pillowfight.utils;

import nl.scoutcraft.eagle.api.network.NetworkChannel;
import nl.scoutcraft.pillowfight.PillowFight;
import org.bukkit.entity.Player;

public class BungeeUtil {

    private static final NetworkChannel BUNGEE_CHANNEL = new NetworkChannel(PillowFight.getInstance(), "BungeeCord");
    private static final String LOBBY_SERVER = PillowFight.getInstance().getConfig().getString("HUB_SERVER");

    public static void sendToLobby(Player player) {
        BUNGEE_CHANNEL.message("Connect", out -> out.writeUTF(LOBBY_SERVER)).send(player);
    }
}
