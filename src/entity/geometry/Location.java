package entity.geometry;

import entity.geometry.map.Chunk;
import entity.geometry.map.RSMap;

/**
 * Represents a position in a {@code WorldMap} to locate anything.
 * 
 * @author Albert Beaupre
 */
public class Location extends Point3D {

	/**
	 * The {@code RSMap} in relation to what this location is placed.
	 */
	public final RSMap map;

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
	public Location(RSMap map, int x, int y, int z) {
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
	 * Returns the current {@code Chunk} of the {@code RSMap} this {@code Location} is in.
	 * 
	 * @return the current chunk
	 */
	public Chunk getCurrentChunk() {
		if (map == null)
			throw new NullPointerException("The RSMap is not defined for this location");
		return map.getChunk(getChunkX(), getChunkY(), z);
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

	/**
	 * Returns the flags at this {@code Location} in the correlating {@code RSMap}.
	 * 
	 * @return the flags of this location
	 */
	public int getFlags() {
		return map.getFlags(x, y, z);
	}

	/**
	 * Returns the clip at this {@code Location} in the correlating {@code RSMap}.
	 * 
	 * @return the clip of this location
	 */
	public int getClip() {
		return map.getClip(x, y, z);
	}

	public void addClip(int clip) {
		map.addClip(x, y, z, clip);
	}

	/**
	 * Returns the 30 bit hash of this {@code Location}.
	 * 
	 * @return the 30 bit hash
	 */
	public int get30BitsHash() {
		return this.y | this.z << 28 | this.x << 14;
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
		if (obj instanceof Point3D) {
			return super.equals(obj);
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

	public Location newInstance() {
		return new Location(map, x, y, z);
	}

	public static Location getDelta(Location l, Location o) {
		return new Location(l.map, o.x - l.x, o.y - l.y, o.z - l.z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode();
	}
}
