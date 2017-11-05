package entity.geometry.map;

import infrastructure.Attachments;
import java.util.HashSet;
import org.apache.commons.collections4.map.MultiKeyMap;
import entity.Entity;
import entity.geometry.EntityLocationChangeEvent;
import entity.geometry.Point3D;
import entity.geometry.Shape3D;
import event.EventListener;
import event.EventMethod;

/**
 * 
 * @author Albert Beaupre
 */
public abstract class RSMap extends AreaManager implements EventListener {

	/**
	 * The number of tiles in a chunk. This is hard coded to be 8.
	 */
	public static final int CHUNK_SIZE = 8;

	/**
	 * The number of bits you need to shift by to go from a tile number to a chunk. Eg, position.x >>
	 * 3 is the chunkX
	 */
	public static final int CHUNK_BITS = 3;

	/**
	 * The number of tiles that can be loaded by a viewer. This is hard coded to be 52 (104 / 2),
	 * which is the maximum view distance (104) of a viewer divided by 2.
	 */
	public static final int LOAD_RADIUS = 168 / 2;

	/**
	 * This is the length in tiles of this {@code RSMap}.
	 */
	public final int width;

	/**
	 * This is the width in tiles of this {@code RSMap}.
	 */
	public final int height;

	private MultiKeyMap<Object, HashSet<Entity>> entities = new MultiKeyMap<>();
	private final Point3D offset;
	private Chunk[][][] chunks;

	/**
	 * 
	 * @param offset
	 * @param length
	 * @param width
	 */
	public RSMap(Point3D offset, int width, int height) {
		this.offset = offset;
		this.width = width;
		this.height = height;

		if (width % CHUNK_SIZE != 0 || height % CHUNK_SIZE != 0)
			throw new IllegalArgumentException("Maps must be a multiple of chunk size.. given length: " + width + ", width: " + height);
		this.chunks = new Chunk[width >> CHUNK_BITS][][];

		Attachments.getEventManager().registerEventListener(this);
	}

	@EventMethod
	public void onEntityLocationChange(EntityLocationChangeEvent event) {
		Entity entity = event.entity;
		if (event.currentLocation == null) {
			entity.destroy();
			return;
		}
		if (event.currentLocation.map == null)
			return;

		if (event.previousLocation != null) {
			HashSet<Entity> fromSet = this.entities.get(entity.getClass(), event.previousLocation);
			if (fromSet == null)
				fromSet = new HashSet<>();
			fromSet.remove(entity);
			this.entities.put(entity.getClass(), event.previousLocation, fromSet);
		} else
			entity.create();
		HashSet<Entity> toSet = this.entities.get(entity.getClass(), event.currentLocation);
		if (toSet == null)
			toSet = new HashSet<>();
		toSet.add(entity);
		this.entities.put(entity.getClass(), event.currentLocation, toSet);
	}

	/**
	 * Creates the chunk at the given {@code (chunkX, chunkY, chunkZ)} coordinates.
	 * 
	 * @param chunkX
	 *           the x coordinate to create the chunk at
	 * @param chunkY
	 *           the y coordinate to create the chunk at
	 * @param chunkZ
	 *           the z coordinate to create the chunk at
	 */
	public abstract void fetch(int chunkX, int chunkY, int chunkZ);

	/**
	 * Creates the chunk at the given {@code (chunkX, chunkY, chunkZ)} coordinates.
	 * 
	 * @param chunkX
	 *           the x coordinate to create the chunk at
	 * @param chunkY
	 *           the y coordinate to create the chunk at
	 * @param chunkZ
	 *           the z coordinate to create the chunk at
	 */
	public abstract Chunk create(int chunkX, int chunkY, int chunkZ);

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void load(int x, int y) {
		for (int i = (x - LOAD_RADIUS - 7) >> 3; i < (x + LOAD_RADIUS + 7) >> 3; i++) {
			for (int j = (y - LOAD_RADIUS - 7) >> 3; j < (y + LOAD_RADIUS + 7) >> 3; j++) {
				try {
					check(i, j);
					for (int z = 0; z < 4; z++) {
						Chunk c = chunks[i - this.offset.x][j - this.offset.y][z];
						if (c == null || c.isLoaded() == false)
							fetch(i, j, z);
					}
				} catch (IndexOutOfBoundsException e) {
					// Near map edge
				}
			}
		}
	}

	/**
	 * Checks if the chunks at the specified ({@code (cx, cy)} coordinates have been defined; if not,
	 * then they are.
	 * 
	 * @param chunkX
	 *           the x coordinate of the chunk to check
	 * @param chunkY
	 *           the y coordinate of the chunk to check
	 */
	protected void check(int chunkX, int chunkY) {
		try {
			if (this.chunks[chunkX - this.offset.x] == null)
				this.chunks[chunkX - this.offset.x] = new Chunk[height >> CHUNK_BITS][];
			if (this.chunks[chunkX - this.offset.x][chunkY - this.offset.y] == null)
				this.chunks[chunkX - this.offset.x][chunkY - this.offset.y] = new Chunk[4];
		} catch (Exception e) {
		}
	}

	/**
	 * Fetches the chunk at the given chunk coordinates. A chunk coordinate is a normal coordinate
	 * bitshifted right by WorldMap.CHUNK_BITS. (Eg pos.x >> WorldMap.CHUNK_BITS)
	 * 
	 * @param chunkX
	 *           The chunk X
	 * @param chunkY
	 *           The chunk Y
	 * @return the chunk, null if out of bounds.
	 */
	public Chunk getChunk(int chunkX, int chunkY, int chunkZ) {
		try {
			check(chunkX, chunkY);

			Chunk c = chunks[chunkX - this.offset.x][chunkY - this.offset.y][chunkZ];
			if (c == null) {
				c = create(chunkX, chunkY, chunkZ);
				if (c == null)
					c = new Chunk(0, 0, 0);
				chunks[chunkX][chunkY][chunkZ] = c;
				return c;
			}
			return c;
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}

	/**
	 * Adds the given clip to the given location in this map. If the location is out of bounds, then
	 * the function returns. If the chunk is null, it returns.
	 * 
	 * @param x
	 *           the x tile coordinate
	 * @param y
	 *           the y tile coordinate
	 * @param z
	 *           the width of the tile
	 * @param clip
	 *           the clip to add
	 */
	public void addClip(int x, int y, int z, int clip) {
		try {
			int chunkX = x >> CHUNK_BITS;
			int chunkY = y >> CHUNK_BITS;

			Chunk c = getChunk(chunkX, chunkY, z);
			if (c == null)
				return;
			c.addClip(x % CHUNK_SIZE, y % CHUNK_SIZE, clip);
		} catch (ArrayIndexOutOfBoundsException e) {

		}
	}

	/**
	 * The opposite of addClip(). Removes the given clip at the given location on this map. If the
	 * location is out of bounds, then the function returns. If the chunk is null, it returns.
	 * 
	 * @param x
	 *           the x tile coordinate
	 * @param y
	 *           the y tile coordinate
	 * @param z
	 *           the width of the tile
	 * @param clip
	 *           the clip to remove.
	 */
	public void removeClip(int x, int y, int z, int clip) {
		try {
			int chunkX = x >> CHUNK_BITS;
			int chunkY = y >> CHUNK_BITS;

			Chunk c = getChunk(chunkX, chunkY, z);
			if (c == null)
				return; // No chunk there.
			c.removeClip(x % CHUNK_SIZE, y % CHUNK_SIZE, clip);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fetches the clip at the given location on the map. If the given location is out of bounds, or
	 * the chunk at that position is null, -1 (0xFFFF FFFF) is returned, meaning there cannot be
	 * movement on that tile.
	 * 
	 * @param x
	 *           the x tile coordinate
	 * @param y
	 *           the y tile coordinate
	 * @param z
	 *           the width of the tile
	 * @return clip the clip
	 */
	public int getClip(int x, int y, int z) {
		try {
			int chunkX = x >> CHUNK_BITS;
			int chunkY = y >> CHUNK_BITS;
			check(chunkX, chunkY);

			Chunk c = chunks[chunkX - this.offset.x][chunkY - this.offset.y][z];
			if (c == null || c.isLoaded() == false)
				return -1;
			return c.getClip(x & 7, y & 7);
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * Returns the flags at the given {@code (x, y, z)} coordinates of this {@code WorldMap}.
	 * 
	 * @param x
	 *           the x coordinate to get the flags
	 * @param y
	 *           the y coordinate to get the flags
	 * @param z
	 *           the z coordinate to get the flags
	 * @return the flags at the specified coordinates
	 */
	public int getFlags(int x, int y, int z) {
		try {
			int chunkX = x >> CHUNK_BITS;
			int chunkY = y >> CHUNK_BITS;
			check(chunkX, chunkY);

			Chunk c = chunks[chunkX - this.offset.x][chunkY - this.offset.y][z];
			if (c == null || c.isLoaded() == false)
				return 0;
			return c.getFlags(x & 7, y & 7);
		} catch (ArrayIndexOutOfBoundsException e) {
			return 0;
		}
	}

	/**
	 * Finds any {@code Entity} of the given class type within the specified {@code bounds} and
	 * returns a {@code HashSet} of the entities found.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public HashSet<Entity> findEntities(Shape3D bounds, Class<? extends Entity> clazz) {
		HashSet<Entity> found = new HashSet<>();
		for (Point3D point : bounds.listPoints()) {
			HashSet<Entity> set = entities.get(clazz, point);
			if (set != null)
				found.addAll(set);
		}
		return found;
	}

	/**
	 * Finds any {@code Entity} of the given class type within the specified {@code radius} of the
	 * {@code location} and returns a {@code HashSet} of the entities found.
	 * 
	 * @param location
	 *           the location of search for entities around
	 * @param radius
	 *           the radius of the location to search around
	 * @return a {@code HashSet} with any entities with the given class type are within the radius of
	 *         the location
	 */
	public HashSet<Entity> findEntities(Point3D location, Class<? extends Entity> clazz, int radius) {
		HashSet<Entity> found = new HashSet<>();
		for (int i = -radius; i < radius; i++) {
			for (int j = -radius; j < radius; j++) {
				HashSet<Entity> set = entities.get(clazz, location.translate(i, j, 0));
				if (set != null)
					found.addAll(set);
			}
		}
		return found;
	}

	/**
	 * Returns the offset location of this {@code WorldMap} in {@code Point3D} form.
	 * 
	 * @return the offset location
	 */
	public final Point3D getOffset() {
		return new Point3D(this.offset.x << CHUNK_BITS, this.offset.y << CHUNK_BITS, 0);
	}
}
