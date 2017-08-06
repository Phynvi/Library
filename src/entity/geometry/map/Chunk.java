package entity.geometry.map;


/**
 * A {@code Chunk} is an 8x8 section of a {@code WorldMap} containing essential
 * clipping and flag information for a specific absolute location.
 * 
 * @author Albert Beaupre
 */
public class Chunk {

    private boolean loaded = false;
    private int cacheX;
    private int cacheY;
    private int cacheZ;

    /**
     * The clip flags for this chunk. Indexes are [x][y], where x and y are 0 to
     * 7. May be null, in which case all values can be assumed to be -1
     */
    private int[][] clip; //x, y

    /**
     * The flags such as indoors or bridge flags. Indexes are [x][y] where x and
     * y are 0 to 7. May be null, in which case all values are assumed to be 0
     */
    private byte[][] flags;

    /**
     * Constructs a new chunk, with the given values representing the location
     * from the cache to load the data from. These do not have to correspond to
     * the real location of the chunk, unless in a StandardMap which is not
     * customizable
     * 
     * @param cacheX
     *            the cache x value of the chunk (x >> 3)
     * @param cacheY
     *            the cache y value of the chunk (y >> 3)
     * @param cacheZ
     *            the cache z value of the chunk
     */
    public Chunk(int cacheX, int cacheY, int cacheZ) {
	this.cacheX = cacheX;
	this.cacheY = cacheY;
	this.cacheZ = cacheZ;
    }

    /**
     * Adds the specified {@code clip} value to the clips of this {@code Chunk}
     * at the specified {@code (x, y)} coordinate argument.
     * 
     * @param x
     *            the x coordinate to add the clip
     * @param y
     *            the y coordinate to add the clip
     * @param clip
     *            the clip value to add
     */
    public void addClip(int x, int y, int clip) {
	if (this.clip == null)
	    this.clip = new int[RSMap.CHUNK_SIZE][RSMap.CHUNK_SIZE];
	this.clip[x][y] |= clip;
    }

    /**
     * Removes the specified {@code clip} value from the clips of this
     * {@code Chunk} at the specified ({@code (x, y)} coordinate arguments
     * 
     * @param x
     *            the x coordinate to remove the clip from
     * @param y
     *            the y coordinate to remove the clip from
     * @param clip
     *            the clip value to remove the clip location
     */
    public void removeClip(int x, int y, int clip) {
	if (this.clip == null)
	    return;
	this.clip[x][y] &= ~clip;
    }

    /**
     * Returns the clip value placed at the specified {@code (x, y)} coordinate
     * arguments.
     * 
     * @param x
     *            the x coordinate to get the clip at
     * @param y
     *            the y coordinate to get the clip at
     * @return the clip value placed at the coordinates
     */
    public int getClip(int x, int y) {
	if (this.clip == null)
	    return 0; //No clips here!
	return this.clip[x][y];
    }

    /**
     * Sets the flag for this chunk at the given coordinate to the given flag.
     * This sets, and does not bitwise OR.
     * 
     * @param x
     *            the x coorindate
     * @param y
     *            the y coordinate
     * @param flag
     *            the flag value
     */
    public void setFlag(int x, int y, int flag) {
	if (flag == 0 && this.flags == null)
	    return; //It's assumed to be 0 already. This saves us allocating extra data.
	if (this.flags == null)
	    this.flags = new byte[RSMap.CHUNK_SIZE][RSMap.CHUNK_SIZE];
	this.flags[x][y] = (byte) flag;
    }

    /**
     * Returns the flags placed at the specified {@code (x, y)} coordinate
     * arguments.
     * 
     * @param x
     *            the x coordinate, 0 to 7
     * @param y
     *            the y coordinate, 0 to 7
     * @return the flags or 0 if none
     */
    public int getFlags(int x, int y) {
	if (this.flags == null)
	    return 0;
	return this.flags[x][y];
    }

    /**
     * Returns true if this chunk has all of the given flags at the given
     * location.
     * 
     * @param x
     *            the x coordinate, 0 to 7
     * @param y
     *            the y coordinate, 0 to 7
     * @param flag
     *            the flags to check for. May be combination of several flags by
     *            using bitwise OR
     * @return true if this chunk has all of the given flags; return false
     *         otherwise
     */
    public boolean hasFlag(int x, int y, int flag) {
	return (this.flags[x][y] & flag) == flag;
    }

    /**
     * Returns true if this {@code Chunk} has been loaded with GameObjects,
     * Clipping values, and Flag values.
     * 
     * @return true if loaded; return false otherwise
     */
    public boolean isLoaded() {
	return this.loaded;
    }

    /**
     * Sets the loaded flag of this {@code Chunk} to the specified
     * {@code loaded} argument. This must <b>ONLY</b> be set true if
     * GameObjects, Clipping, and Flags have been set to this {@code Chunk}.
     * 
     * @param loaded
     *            the flag to set
     */
    public void setLoaded(boolean loaded) {
	this.loaded = loaded;
    }

    /**
     * Returns the x coordinate in the cache of this {@code Chunk}.
     * 
     * @return the x coordinate in the cache
     */
    public int getCacheX() {
	return this.cacheX;
    }

    /**
     * Returns the y coordinate in the cache of this {@code Chunk}.
     * 
     * @return the y coordinate in the cache
     */
    public int getCacheY() {
	return this.cacheY;
    }

    /**
     * Returns the z coordinate in the cache of this {@code Chunk}.
     * 
     * @return the z coordinate in the cache
     */
    public int getCacheZ() {
	return this.cacheZ;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
	return String.format("Chunk[x=%s, y=%s, z=%s, loaded=%s]", cacheX, cacheY, cacheZ, loaded);
    }
}