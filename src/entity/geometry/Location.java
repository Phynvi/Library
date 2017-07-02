package entity.geometry;

import java.util.HashSet;

import entity.Entity;
import entity.geometry.map.WorldMap;

/**
 * Represents a position in a {@code WorldMap} to locate anything.
 * 
 * @author Albert Beaupre
 */
public class Location extends Point3D {

    private final WorldMap map;

    /**
     * Constructs a new {@code Location} from the specified coordinates.
     * 
     * @param map
     *            the {@code WorldMap} that this location is relative to
     * 
     * @param x
     *            the x coordinate of the location
     * @param y
     *            the y coordinate of the location
     * @param z
     *            the z coordinate of the location
     */
    public Location(WorldMap map, int x, int y, int z) {
	super(x, y, z);
	this.map = map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see entity.geometry.Point3D#translate(int, int, int)
     */
    public Location translate(int dx, int dy, int dz) {
	return new Location(this.map, this.x + dx, this.y + dy, this.z + dz);
    }

    /**
     * Returns the region x coordinate of this {@code Location}.
     * 
     * @return the region x coordinate
     */
    public int getRegionX() {
	return (this.x >> 3);
    }

    /**
     * Returns the region y coordinate of this {@code Location}.
     * 
     * @return the region y coordinate
     */
    public int getRegionY() {
	return (this.y >> 3);
    }

    /**
     * Returns the chunk x coordinate of this {@code Location}.
     * 
     * @return the chunk x coordinate
     */
    public int getChunkX() {
	return getRegionX() >> 3;
    }

    /**
     * Returns the chunk y coordinate of this {@code Location}.
     * 
     * @return the chunk y coordinate
     */
    public int getChunkY() {
	return getRegionY() >> 3;
    }

    /**
     * Returns the 18 bit hash of this {@code Location}.
     * 
     * @return the 18 bit hash
     */
    public int get18BitsHash() {
	int regionId = ((getRegionX() / 8) << 8) + (getRegionY() / 8);
	return (((regionId & 0xff) << 6) >> 6) | (this.z << 16) | ((((regionId >> 8) << 6) >> 6) << 8);
    }

    public int getFlags() {
	return map.getFlags(x, y, z);
    }

    public int getClip() {
	return map.getClip(x, y, z);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
	return z << 30 | x << 15 | y;
    }

    /**
     * Returns the 30 bit hash of this {@code Location}.
     * 
     * @return the 30 bit hash
     */
    public int get30BitsHash() {
	return this.y | this.z << 28 | this.x << 14;
    }

    /**
     * Returns a {@code HashSet} filled with entities of the specified
     * {@code clazz} type within the radius of this {@code Location}.
     * 
     * @param clazz
     *            the class type of the entities
     * @param radius
     *            the radius to search for the entities
     * @return the {@code HashSet} filled with the entities within the radius
     */
    public <E extends Entity> HashSet<E> getEntities(Class<E> clazz, int radius) {
	return this.map.getEntities(e -> e.getLocation().inRange(Location.this, radius), clazz);
    }

    /**
     * Returns the {@code WorldMap} that this {@code Location} is relative to.
     * 
     * @return the world map
     */
    public WorldMap getMap() {
	return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see entity.geometry.Point3D#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
	if (obj instanceof Location) {
	    Location l = (Location) obj;
	    return l.x == x && l.y == y && l.z == z && l.map == map;
	}
	return super.equals(obj);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
	return String.format("Location[x=%s, y=%s, z=%s]", this.x, this.y, this.z);
    }

}
