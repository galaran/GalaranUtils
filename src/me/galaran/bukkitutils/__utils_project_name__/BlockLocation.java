package me.galaran.bukkitutils.__utils_project_name__;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/** Immutable */
public class BlockLocation implements Cloneable {

    private final World world;
    private final int x;
    private final int y;
    private final int z;

    public BlockLocation(World world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public BlockLocation(Location loc) {
        this(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public BlockLocation(Block block) {
        this(block.getWorld(), block.getX(), block.getY(), block.getZ());
    }

    public Block getBlock() {
        return world.getBlockAt(x, y, z);
    }

    public Location getLocation() {
        return new Location(world, x, y, z);
    }

    public BlockLocation add(int dx, int dy, int dz) {
        return new BlockLocation(world, x + dx, y + dy, z + dz);
    }

    public BlockLocation subtract(int dx, int dy, int dz) {
        return new BlockLocation(world, x - dx, y - dy, z - dz);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockLocation that = (BlockLocation) o;

        if (world != null ? !world.equals(that.world) : that.world != null) return false;
        if (x != that.x) return false;
        if (y != that.y) return false;
        return z == that.z;

    }

    @Override
    public int hashCode() {
        int result = world != null ? world.hashCode() : 0;
        result = 31 * result + x;
        result = 31 * result + y;
        result = 31 * result + z;
        return result;
    }

    @Override
    public BlockLocation clone() {
        try {
            return (BlockLocation) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }
}
