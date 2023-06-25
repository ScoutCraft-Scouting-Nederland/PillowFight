package nl.scoutcraft.pillowfight.game.team;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.md_5.bungee.api.ChatColor;
import nl.scoutcraft.eagle.api.Eagle;
import nl.scoutcraft.eagle.api.locale.IMessage;
import nl.scoutcraft.pillowfight.data.Item;
import nl.scoutcraft.pillowfight.data.Keys;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Team {

    private final int id;
    @Nullable private final IMessage<String> name;
    private final ChatColor color;
    @Nullable private final Color armorColor;
    private final Material wool;
    private final List<Player> players;
    private final List<UUID> originalPlayers;

    protected Team(int id, @Nullable IMessage<String> name, ChatColor color, @Nullable Color armorColor, Material wool) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.armorColor = armorColor;
        this.wool = wool;
        this.players = new ArrayList<>();
        this.originalPlayers = new ArrayList<>();
    }

    public Team addPlayer(Player player) {
        this.players.add(player);
        this.originalPlayers.add(player.getUniqueId());

        player.getPersistentDataContainer().set(Keys.TEAM_ID, PersistentDataType.INTEGER, this.id);
        player.playerListName(Component.text(player.getName(), TextColor.color(this.color.getColor().getRGB())));
        Eagle.getInventoryMenuManager().clear(player);
        Item.givePillow(player, this.wool);

        return this;
    }

    public boolean kill(Player player) {
        return this.players.remove(player);
    }

    public boolean isDead() {
        return this.players.isEmpty();
    }

    public int getId() {
        return this.id;
    }

    @Nullable
    public IMessage<String> getName() {
        return this.name;
    }

    public ChatColor getColor() {
        return this.color;
    }

    @Nullable
    public Color getArmorColor() {
        return this.armorColor;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public List<UUID> getOriginalPlayers() {
        return this.originalPlayers;
    }
}
