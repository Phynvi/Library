package entity.geometry;

/**
 * Represents a position in a {@code WorldMap} to locate anything.
 * 
 * @author Albert Beaupre
 */
public class Location extends Point3D {

    private WorldMap map;

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

    /**
     * Returns the 30 bit hash of this {@code Location}.
     * 
     * @return the 30 bit hash
     */
    public int get30BitsHash() {
	return this.y | this.z << 28 | this.x << 14;
    }

    /**
     * Sets the {@code WorldMap} of this {@code Location} to the specified
     * {@code map} argument.
     * 
     * @param map
     *            the {@code WorldMap} to set
     */
    public void setMap(WorldMap map) {
	this.map = map;
    }

    /**
     * Returns the {@code WorldMap} that this [{@code Location} is relative to.
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
