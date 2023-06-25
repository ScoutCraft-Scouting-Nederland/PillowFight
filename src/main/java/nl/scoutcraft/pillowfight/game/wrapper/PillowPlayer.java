package nl.scoutcraft.pillowfight.game.wrapper;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PillowPlayer {

    private final UUID uuid;
    private int kills, deaths, slaps, hits, timePlayed, powerupPickups, powerupUses, place;

    private static final Map<UUID, PillowPlayer> data = new HashMap<>();

    public PillowPlayer(UUID uuid) {
        this.uuid = uuid;
    }

    public static PillowPlayer get(Player player) {
        return get(player.getUniqueId());
    }

    public static PillowPlayer get(UUID uuid) {
        return data.computeIfAbsent(uuid, PillowPlayer::new);
    }

    public static Collection<PillowPlayer> values() {
        return data.values();
    }

    public void addKill() {
        this.kills++;
    }

    public void addDeath() {
        this.deaths++;
    }

    public void addSlap() {
        this.slaps++;
    }

    public void addHit() {
        this.hits++;
    }

    public void setTimePlayed(int timePlayed) {
        this.timePlayed = timePlayed;
    }

    public void addPowerupPickup() {
        this.powerupPickups++;
    }

    public void addPowerupUse() {
        this.powerupUses++;
    }

    public void setPlace(int place) {
        this.place = place;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public int getKills() {
        return this.kills;
    }

    public int getDeaths() {
        return this.deaths;
    }

    public int getSlaps() {
        return this.slaps;
    }

    public int getHits() {
        return this.hits;
    }

    public int getTimePlayed() {
        return this.timePlayed;
    }

    public int getPowerupPickups() {
        return this.powerupPickups;
    }

    public int getPowerupUses() {
        return this.powerupUses;
    }

    public int getPlace() {
        return this.place;
    }
}
