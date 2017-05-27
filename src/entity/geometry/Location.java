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
	return (x >> 3);
    }

    /**
     * Returns the region y coordinate of this {@code Location}.
     * 
     * @return the region y coordinate
     */
    public int getRegionY() {
	return (y >> 3);
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

    public int get18BitsHash() {
	int regionId = ((getRegionX() / 8) << 8) + (getRegionY() / 8);
	return (((regionId & 0xff) << 6) >> 6) | (z << 16) | ((((regionId >> 8) << 6) >> 6) << 8);
    }

    public int get30BitsHash() {
	return y | z << 28 | x << 14;
    }

    /**
     * Returns the {@code WorldMap} that this [{@code Location} is relative to.
     * 
     * @return the world map
     */
    public WorldMap getMap() {
	return map;
    }

    @Override
    public String toString() {
	return String.format("Location[x=%s, y=%s, z=%s]", x, y, z);
    }

}
