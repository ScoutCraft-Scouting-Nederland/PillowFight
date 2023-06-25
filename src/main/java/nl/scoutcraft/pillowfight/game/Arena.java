package nl.scoutcraft.pillowfight.game;

import nl.scoutcraft.eagle.api.map.IMap;
import nl.scoutcraft.pillowfight.utils.Area;
import nl.scoutcraft.pillowfight.utils.Position;

public class Arena implements IMap {

    private final String name, slimeName;
    private final int time;

    private final Position spawn;
    private final int voidY;
    private final Area powerupArea;

    public Arena(String name, String slimeName, int time, Position spawn, int voidY, Area powerupArea) {
        this.name = name;
        this.slimeName = slimeName;
        this.time = time;
        this.spawn = spawn;
        this.voidY = voidY;
        this.powerupArea = powerupArea;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String getSlimeName() {
        return this.slimeName;
    }

    @Override
    public int getTimeTicks() {
        return this.time;
    }

    public Position getSpawn() {
        return this.spawn;
    }

    public int getVoidY() {
        return this.voidY;
    }

    public Area getPowerupArea() {
        return this.powerupArea;
    }
}
