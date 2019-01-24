package entity.geometry.map;

import java.util.HashMap;
import java.util.HashSet;

import entity.Entity;
import entity.EntityList;
import entity.actor.npc.NPC;
import entity.geometry.EntityLocationChangeEvent;
import entity.geometry.Location;
import entity.geometry.Point3D;
import entity.geometry.Shape3D;
import event.EventListener;
import event.EventMethod;
import infrastructure.GlobalVariables;
import network.World;

/**
 * 
 * @author Albert Beaupre
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class RSMap extends AreaManager implements EventListener {

	public static final int FLAG_CLIP = 0x1;
	public static final int FLAG_BRIDGE = 0x2;

	/**
	 * Flag given if there is a roof over this piece of terrain
	 */
	public static final int FLAG_ROOF = 0x4;

	/**
	 * Steep cliff flag? Elevation flag?
	 */
	public static final int FLAG_UNKNOWN = 0x8;

	/**
	 * Wall flag?
	 */
	public static final int FLAG_UNKNOWN2 = 0x10;
	/**
	 * The number of tiles in a chunk. This is hard coded to be eight.
	 */
	public static final int CHUNK_SIZE = 8;

	/**
	 * The number of bits you need to shift by to go from a tile number to a chunk. Eg, position.x >> 3
	 * is the chunkX
	 */
	public static final int CHUNK_BITS = 3;

	/**
	 * The number of tiles that can be loaded by a viewer. This is hard coded to be 52 (104 / 2), which
	 * is the maximum view distance (104) of a viewer divided by 2.
	 */
	public static final int LOAD_RADIUS = 104 / 2;

	/**
	 * This is the length in tiles of this {@code RSMap}.
	 */
	public final int width;

	/**
	 * This is the width in tiles of this {@code RSMap}.
	 */
	public final int height;

	private final EntityList<NPC> npcs = new EntityList<>(32000);
	private HashMap<Location, HashSet<? extends Entity>> entities = new HashMap<>();
	private final Point3D offset;
	private Chunk[][][] chunks;

	private final World world;

	/**
	 * 
	 * @param offset
	 * @param length
	 * @param width
	 */
	public RSMap(World world, Point3D offset, int width, int height) {
		this.world = world;
		this.offset = offset;
		this.width = width;
		this.height = height;

		if (width % CHUNK_SIZE != 0 || height % CHUNK_SIZE != 0)
			throw new IllegalArgumentException("Maps must be a multiple of chunk size.. given length: " + width + ", width: " + height);
		this.chunks = new Chunk[width >> CHUNK_BITS][height >> CHUNK_BITS][4];

		GlobalVariables.getEventManager().registerEventListener(this);
	}

	@EventMethod
	public void onEntityLocationChange(EntityLocationChangeEvent event) {
		Entity entity = event.entity;
		if (entity.getLocation() != null && entity.getLocation().map != this)
			return;
		if (event.currentLocation == null) {
			if (entity instanceof NPC) {
				npcs.remove((NPC) entity);
			} else {
				if (event.previousLocation != null) {
					HashSet fromSet = this.entities.get(event.previousLocation);
					if (fromSet == null)
						fromSet = new HashSet<>();
					fromSet.remove(entity);
					this.entities.put(event.previousLocation, fromSet);
				}
			}
			entity.destroy();
			return;
		}
		if (event.currentLocation.map == null)
			return;

		if (event.previousLocation != null) {
			if (entity instanceof NPC) {
				npcs.remove((NPC) entity);
			} else {
				HashSet fromSet = this.entities.get(event.previousLocation);
				if (fromSet == null)
					fromSet = new HashSet<>();
				fromSet.remove(entity);
				this.entities.put(event.previousLocation, fromSet);
			}

			Location previousRegionLocation = entity.getTemporary("previous_region_location", null);
			if (previousRegionLocation == null)
				entity.temporary("previous_region_location", previousRegionLocation = event.previousLocation);

			int diffX = Math.abs(event.currentLocation.getRegionX() - previousRegionLocation.getRegionX());
			int diffY = Math.abs(event.currentLocation.getRegionY() - previousRegionLocation.getRegionY());

			if (diffX >= 5 || diffY >= 5) {

				entity.temporary("previous_region_location", event.currentLocation);
				/*
				 * Set the map region changing flag so the new map region packet is sent upon the next update.
				 */
				updateMapRegionChange(entity);
			}

		} else entity.create();
		if (entity instanceof NPC) {
			npcs.add((NPC) entity);
		} else {
			HashSet toSet = this.entities.get(event.currentLocation);
			if (toSet == null)
				toSet = new HashSet<>();
			toSet.add(entity);
			this.entities.put(event.currentLocation, toSet);
		}
	}

	/**
	 * Creates the chunk at the given {@code (chunkX, chunkY, chunkZ)} coordinates.
	 * 
	 * @param chunkX
	 *            the x coordinate to create the chunk at
	 * @param chunkY
	 *            the y coordinate to create the chunk at
	 * @param chunkZ
	 *            the z coordinate to create the chunk at
	 */
	public abstract void fetch(int chunkX, int chunkY, int chunkZ);

	/**
	 * Creates the chunk at the given {@code (chunkX, chunkY, chunkZ)} coordinates.
	 * 
	 * @param chunkX
	 *            the x coordinate to create the chunk at
	 * @param chunkY
	 *            the y coordinate to create the chunk at
	 * @param chunkZ
	 *            the z coordinate to create the chunk at
	 */
	public abstract Chunk create(int chunkX, int chunkY, int chunkZ);

	/**
	 * Updates the map region changing for the specified {@code Entity}.
	 * 
	 * @param entity
	 *            the entity to update the change in the map region
	 */
	public abstract void updateMapRegionChange(Entity entity);

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
	 *            the x coordinate of the chunk to check
	 * @param chunkY
	 *            the y coordinate of the chunk to check
	 */
	protected void check(int chunkX, int chunkY) {
		try {
			if (this.chunks[chunkX - this.offset.x] == null)
				this.chunks[chunkX - this.offset.x] = new Chunk[height >> CHUNK_BITS][];
			if (this.chunks[chunkX - this.offset.x][chunkY - this.offset.y] == null)
				this.chunks[chunkX - this.offset.x][chunkY - this.offset.y] = new Chunk[4];
		} catch (Exception e) {}
	}

	/**
	 * Fetches the chunk at the given chunk coordinates. A chunk coordinate is a normal coordinate
	 * bitshifted right by WorldMap.CHUNK_BITS. (Eg pos.x >> WorldMap.CHUNK_BITS)
	 * 
	 * @param chunkX
	 *            The chunk X
	 * @param chunkY
	 *            The chunk Y
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
	 * Adds the given clip to the given location in this map. If the location is out of bounds, then the
	 * function returns. If the chunk is null, it returns.
	 * 
	 * @param x
	 *            the x tile coordinate
	 * @param y
	 *            the y tile coordinate
	 * @param z
	 *            the width of the tile
	 * @param clip
	 *            the clip to add
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
	 *            the x tile coordinate
	 * @param y
	 *            the y tile coordinate
	 * @param z
	 *            the width of the tile
	 * @param clip
	 *            the clip to remove.
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
	 * Fetches the clip at the given location on the map. If the given location is out of bounds, or the
	 * chunk at that position is null, -1 (0xFFFF FFFF) is returned, meaning there cannot be movement on
	 * that tile.
	 * 
	 * @param x
	 *            the x tile coordinate
	 * @param y
	 *            the y tile coordinate
	 * @param z
	 *            the width of the tile
	 * @return clip the clip
	 */
	public int getClip(int x, int y, int z) {
		try {
			int chunkX = x >> CHUNK_BITS;
			int chunkY = y >> CHUNK_BITS;
			check(chunkX, chunkY);

			Chunk c = chunks[chunkX - this.offset.x][chunkY - this.offset.y][z];
			if (c == null || c.isLoaded() == false)
				return 0;
			return c.getClip(x & 7, y & 7);
		} catch (Exception e) {
			return 0;
		}
	}

	/**
	 * Returns the flags at the given {@code (x, y, z)} coordinates of this {@code WorldMap}.
	 * 
	 * @param x
	 *            the x coordinate to get the flags
	 * @param y
	 *            the y coordinate to get the flags
	 * @param z
	 *            the z coordinate to get the flags
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
	 * Finds any {@code Entity} of the given class type within the specified {@code bounds} and returns
	 * a {@code HashSet} of the entities found.
	 * 
	 * @param from
	 * @param to
	 * @return
	 */
	public <T extends Entity> HashSet<T> findEntities(Shape3D bounds, Class<T> clazz) {
		HashSet<T> found = new HashSet<>();
		for (Point3D point : bounds.listPoints()) {
			HashSet<T> set = (HashSet<T>) entities.get(point);
			for (T t : set) {
				if (clazz.isInstance(t)) {
					found.add(t);
				}
			}
		}
		return found;
	}

	/**
	 * Finds any {@code Entity} of the given class type within the specified {@code radius} of the
	 * {@code location} and returns a {@code HashSet} of the entities found.
	 * 
	 * @param location
	 *            the location of search for entities around
	 * @param radius
	 *            the radius of the location to search around
	 * @return a {@code HashSet} with any entities with the given class type are within the radius of
	 *         the location
	 */
	public <T> HashSet<T> findEntities(Point3D location, Class<T> clazz, int radius) {
		HashSet<T> found = new HashSet<>();
		for (int i = -radius; i <= radius; i++) {
			for (int j = -radius; j <= radius; j++) {
				HashSet<T> set = (HashSet<T>) entities.get(location.translate(i, j, 0));
				if (set != null) {
					for (T t : set) {
						if (clazz.isInstance(t)) {
							found.add(t);
						}
					}
				}
			}
		}
		return found;
	}

	public EntityList<NPC> getNPCS() {
		return npcs;
	}

	/**
	 * Returns the offset location of this {@code WorldMap} in {@code Point3D} form.
	 * 
	 * @return the offset location
	 */
	public final Point3D getOffset() {
		return new Point3D(this.offset.x << CHUNK_BITS, this.offset.y << CHUNK_BITS, 0);
	}

	public World getWorld() {
		return world;
	}

}
