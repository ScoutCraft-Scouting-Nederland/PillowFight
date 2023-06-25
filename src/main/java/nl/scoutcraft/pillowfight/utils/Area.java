package nl.scoutcraft.pillowfight.utils;

import org.bukkit.Location;

import javax.annotation.Nullable;

import static java.lang.Integer.parseInt;

public class Area {

    private String world;
    private int minX, maxX, minY, maxY, minZ, maxZ;

    public Area(String world) {
        this.world = world;
    }

    public Area(String world, int x1, int x2, int y1, int y2, int z1, int z2) {
        this.world = world;
        this.minX = Math.min(x1, x2);
        this.maxX = Math.max(x1, x2);
        this.minY = Math.min(y1, y2);
        this.maxY = Math.max(y1, y2);
        this.minZ = Math.min(z1, z2);
        this.maxZ = Math.max(z1, z2);
    }

    public boolean isInArea(Location loc) {
        if (!loc.getWorld().getName().equalsIgnoreCase(this.world))
            return false;

        int locX = loc.getBlockX(), locY = loc.getBlockY(), locZ = loc.getBlockZ();

        return locX >= minX && locX <= maxX && locY >= minY && locY <= maxY && locZ >= minZ && locZ <= maxZ;
    }

    public String getWorld() {
        return this.world;
    }

    public Area setWorld(String world) {
        this.world = world;
        return this;
    }

    public double getMinX() {
        return this.minX;
    }

    public Area setMinX(int minX) {
        this.minX = minX;
        return this;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public Area setMaxX(int maxX) {
        this.maxX = maxX;
        return this;
    }

    public double getMinY() {
        return this.minY;
    }

    public Area setMinY(int minY) {
        this.minY = minY;
        return this;
    }

    public double getMaxY() {
        return this.maxY;
    }

    public Area setMaxY(int maxY) {
        this.maxY = maxY;
        return this;
    }

    public double getMinZ() {
        return this.minZ;
    }

    public Area setMinZ(int minZ) {
        this.minZ = minZ;
        return this;
    }

    public double getMaxZ() {
        return this.maxZ;
    }

    public Area setMaxZ(int maxZ) {
        this.maxZ = maxZ;
        return this;
    }

    public String serialize() {
        return this.world + ":" + this.minX + "_" + this.maxX + ":" + this.minY + "_" + this.maxY + ":" + this.minZ + "_" + this.maxZ;
    }

    @Nullable
    public static Area deserialize(String serialized) {
        String[] parts = serialized.split(":");

        if (parts.length < 4)
            return null;

        String[] x = parts[1].split("_");
        String[] y = parts[2].split("_");
        String[] z = parts[3].split("_");

        if (x.length < 2 || y.length < 2 || z.length < 2)
            return null;

        return new Area(parts[0], parseInt(x[0]), parseInt(x[1]), parseInt(y[0]), parseInt(y[1]), parseInt(z[0]), parseInt(z[1]));
    }
}
