package entity.geometry;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Predicate;
import java.util.logging.Logger;

import entity.EntityList;
import entity.actor.npc.NPC;
import entity.actor.persona.Persona;
import entity.geometry.area.AreaManager;

/**
 * The {@code WorldMap} class is used to create maps that are either static
 * (Original RuneScape Map) or dynamic (Fight Caves).
 * 
 * @author Albert Beaupre
 */
public abstract class WorldMap extends AreaManager {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    /**
     * The number of tiles in a chunk. This is hard coded to be 8.
     */
    public static final int CHUNK_SIZE = 8;

    /**
     * The number of bits you need to shift by to go from a tile number to a
     * chunk. Eg, position.x >> 3 is the chunkX
     */
    public static final int CHUNK_BITS = 3;

    /**
     * The number of tiles that can be loaded by a viewer. This is hard coded to
     * be 52 (104 / 2), which is the minimum view distance (104) of a viewer
     * divided by 2.
     */
    public static final int LOAD_RADIUS = 52;

    private final HashMap<Class<?>, HashSet<Locatable>> entities;
    private final EntityList<Persona> personas;
    private final EntityList<NPC> npcs;
    private final int width, height;
    private Chunk[][][] chunks;
    private Point3D location;

    /**
     * Constructs a new {@code WorldMap} based on the given arguments. The
     * {@code location} of the {@code WorldMap} will be the starting coordinates
     * that the map will be build at from the bottom left. The {@code width} and
     * {@code height} specified are the dimensions the {@code WorldMap} will be
     * (exactly) by tile.
     * 
     * @param location
     *            the location starting from bottom-left that the map will be
     *            build at
     * @param width
     *            the width of the map by tile
     * @param height
     *            the height of the map by tile
     */
    public WorldMap(Point3D location, int width, int height) {
	this.entities = new HashMap<>();
	this.personas = new EntityList<>(2048);
	this.npcs = new EntityList<>(32767);

	this.width = width;
	this.height = height;

	if (width % CHUNK_SIZE != 0 || height % CHUNK_SIZE != 0)
	    throw new IllegalArgumentException("Maps must be a multiple of chunk size.. given width: " + width + ", height: " + height);
	this.chunks = new Chunk[width >> CHUNK_BITS][][];
    }

    /**
     * Creates the chunk at the given {@code (chunkX, chunkY, chunkZ)}
     * coordinates.
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
     * Constructs a new {@code Chunk} at the given
     * {@code (chunkX, chunkY, chunkZ)} coordinates and returns the constructed
     * {@code Chunk}.
     * 
     * @param chunkX
     *            the x coordinate to construct the chunk at
     * @param chunkY
     *            the y coordinate to construct the chunk at
     * @param chunkZ
     *            the z coordinate to construct the chunk at
     * @return the {@code Chunk} constructed
     */
    public abstract Chunk construct(int chunkX, int chunkY, int chunkZ);

    /**
     * Checks if the chunks at the specified ({@code (cx, cy)} coordinates have
     * been defined; if not, then they are.
     * 
     * @param cx
     *            the x coordinate of the chunk to check
     * @param cy
     *            the y coordinate of the chunk to check
     */
    protected void check(int cx, int cy) {
	try {
	    if (this.chunks[cx - this.location.x] == null)
		this.chunks[cx - this.location.x] = new Chunk[height >> CHUNK_BITS][];
	    if (this.chunks[cx - this.location.x][cy - this.location.y] == null)
		this.chunks[cx - this.location.x][cy - this.location.y] = new Chunk[4];
	} catch (IndexOutOfBoundsException e) {}
    }

    /**
     * Loads the required range around the given chunk coordinates.
     * 
     * @param x
     *            the x coordinate of the center chunk
     * @param y
     *            the y coordinate of the center chunk
     */
    public void load(int x, int y) {
	for (int i = (x - LOAD_RADIUS - 7) >> 3; i < (x + LOAD_RADIUS + 7) >> 3; i++) {
	    for (int j = (y - LOAD_RADIUS - 7) >> 3; j < (y + LOAD_RADIUS + 7) >> 3; j++) {
		try {
		    check(i, j);
		    for (int z = 0; z < 4; z++) {
			Chunk c = chunks[i - this.location.x][j - this.location.y][z];
			if (c == null || c.isLoaded() == false)
			    fetch(i, j, z);
		    }
		} catch (IndexOutOfBoundsException e) {
		    //Near map edge
		}
	    }
	}
    }

    /**
     * Sets the chunk at the given chunkX, chunkY coordinates to the given
     * chunk. This does not currently remove entities etc.
     * 
     * @param chunkX
     *            The chunkX
     * @param chunkY
     *            The chunkY
     * @param c
     *            the new chunk to set
     */
    protected void setChunk(int chunkX, int chunkY, int z, Chunk c) {
	check(chunkX, chunkY);

	if (chunks[chunkX - this.location.x][chunkY - this.location.y][z] == c)
	    return; //Already set.

	//TODO: Update players, remove items, etc.
	chunks[chunkX - this.location.x][chunkY - this.location.y][z] = c;
    }

    /**
     * Fetches the chunk at the given chunk coordinates. A chunk coordinate is a
     * normal coordinate bitshifted right by WorldMap.CHUNK_BITS. (Eg pos.x >>
     * WorldMap.CHUNK_BITS)
     * 
     * @param chunkX
     *            The chunk X
     * @param chunkY
     *            The chunk Y
     * @return the chunk, null if out of bounds.
     */
    public Chunk getChunk(int chunkX, int chunkY, int z) {
	try {
	    check(chunkX, chunkY);

	    Chunk c = chunks[chunkX - this.location.x][chunkY - this.location.y][z];
	    if (c == null) {
		c = construct(chunkX, chunkY, z);
		if (c == null) {
		    //Couldn't load a chunk, create a blank one
		    c = new Chunk(0, 0, 0);
		}
		setChunk(chunkX, chunkY, z, c);

		return c;
	    }
	    return c;
	} catch (IndexOutOfBoundsException e) {
	    return null;
	}
    }

    /**
     * Adds the given clip to the given location in this map. If the location is
     * out of bounds, then the function returns. If the chunk is null, it
     * returns.
     * 
     * @param x
     *            the x tile coordinate
     * @param y
     *            the y tile coordinate
     * @param z
     *            the height of the tile
     * @param clip
     *            the clip to add
     */
    public void addClip(int x, int y, int z, int clip) {
	try {
	    int cx = x >> CHUNK_BITS;
	    int cy = y >> CHUNK_BITS;

	    Chunk c = getChunk(cx, cy, z);
	    if (c == null)
		return;
	    c.addClip(x % CHUNK_SIZE, y % CHUNK_SIZE, clip);
	} catch (ArrayIndexOutOfBoundsException e) {
	    //We can probably ignore this.
	}
    }

    /**
     * The opposite of addClip(). Removes the given clip at the given location
     * on this map. If the location is out of bounds, then the function returns.
     * If the chunk is null, it returns.
     * 
     * @param x
     *            the x tile coordinate
     * @param y
     *            the y tile coordinate
     * @param z
     *            the height of the tile
     * @param clip
     *            the clip to remove.
     */
    public void removeClip(int x, int y, int z, int clip) {
	try {
	    int cx = x >> CHUNK_BITS;
	    int cy = y >> CHUNK_BITS;

	    Chunk c = getChunk(cx, cy, z);
	    if (c == null)
		return; //No chunk there.
	    c.removeClip(x % CHUNK_SIZE, y % CHUNK_SIZE, clip);
	} catch (ArrayIndexOutOfBoundsException e) {}
    }

    /**
     * Fetches the clip at the given location on the map. If the given location
     * is out of bounds, or the chunk at that position is null, -1 (0xFFFF FFFF)
     * is returned, meaning there cannot be movement on that tile.
     * 
     * @param x
     *            the x tile coordinate
     * @param y
     *            the y tile coordinate
     * @param z
     *            the height of the tile
     * @return clip the clip
     */
    public int getClip(int x, int y, int z) {
	try {
	    int cx = x >> CHUNK_BITS;
	    int cy = y >> CHUNK_BITS;
	    check(cx, cy);

	    Chunk c = chunks[cx - this.location.x][cy - this.location.y][z];
	    if (c == null || c.isLoaded() == false)
		return -1;
	    return c.getClip(x & 7, y & 7);
	} catch (ArrayIndexOutOfBoundsException e) {
	    return -1;
	}
    }

    /**
     * Returns the flags at the given {@code (x, y, z)} coordinates of this
     * {@code WorldMap}.
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
	    int cx = x >> CHUNK_BITS;
	    int cy = y >> CHUNK_BITS;
	    check(cx, cy);

	    Chunk c = chunks[cx - this.location.x][cy - this.location.y][z];
	    if (c == null || c.isLoaded() == false)
		return 0;
	    return c.getFlags(x & 7, y & 7);
	} catch (ArrayIndexOutOfBoundsException e) {
	    return 0;
	}
    }

    /**
     * Returns the offset location of this {@code WorldMap} in {@code Point3D}
     * form.
     * 
     * @return the offset location
     */
    public final Point3D getOffset() {
	return new Point3D(location.x << CHUNK_BITS, location.y << CHUNK_BITS, 0);
    }

    /**
     * Puts the specified {@code entity} within this {@code  WorldMap} allowing
     * it to be searched for or added/removed from this {@code  WorldMap}.
     * 
     * @param entity
     *            the entity to put in this {@code WorldMap}
     * 
     * @throws NullPointerException
     *             if there is no area defined in this {@code WorldMap} class
     *             where the entity is located
     */
    public <E extends Locatable> void put(E entity) {
	if (!this.contains(entity.getLocation()))
	    LOGGER.warning("There is no area defined where the specified entity is located");

	if (entity instanceof Persona) {
	    personas.add((Persona) entity);
	} else if (entity instanceof NPC) {
	    npcs.add((NPC) entity);
	} else {
	    HashSet<Locatable> set = entities.getOrDefault(entity.getClass(), new HashSet<>());
	    set.add(entity);
	    entities.put(entity.getClass(), set);
	}
    }

    /**
     * Removes the specified {@code entity} from this {@code WorldMap}.
     * 
     * @param entity
     *            the entity to remove
     */
    public void remove(Locatable entity) {
	HashSet<Locatable> set = entities.get(entity.getClass());
	if (entity instanceof Persona)
	    personas.remove((Persona) entity);
	if (entity instanceof NPC)
	    npcs.remove((NPC) entity);
	if (set == null) {
	    LOGGER.warning("The entity " + entity + " was not removed from WorldMap: " + toString());
	    return;
	}
	if (!set.remove(entity))
	    LOGGER.warning("The entity " + entity + " was not removed from WorldMap: " + toString());
    }

    /**
     * Retrieves a {@code Locatable} entity with the specified {@code clazz}
     * type that passes the {@code filter}. If no {@code Locatable} passes the
     * {@code filter} then {@code null} is returned.
     * 
     * @param filter
     *            the filter to check with entities
     * @param clazz
     *            the type of {@code Locatable} to check for
     * @return the found entity; return null if not found
     */
    @SuppressWarnings("unchecked")
    public <U extends Locatable> U getEntity(Predicate<U> filter, Class<U> clazz) {
	HashSet<Locatable> set = entities.get(clazz);
	U found = null;
	for (Locatable type : set) {
	    if (!filter.test((U) type))
		return null;
	}
	return found;
    }

    /**
     * Finds any entities in this {@code Area} by the specified {@code bounds}
     * that are an instance of the specified {@code clazz} and uses the
     * {@code guess} value to guess how many entities will approximately be
     * found within this {@code Area}.
     * 
     * @param bounds
     *            the {@code Shape3D} bounds to search in this {@code Area}
     * @param guess
     *            the guess of how many entities will be found
     * @param clazz
     *            the class type of the entity to be found
     * 
     * @return a {@link java.util.HashSet} filled with the entities found within
     *         the {@code bounds}
     */
    @SuppressWarnings("unchecked")
    public <U extends Locatable> HashSet<U> findEntities(Shape3D bounds, int guess, Class<U> clazz) {
	HashSet<U> objects = new HashSet<>(guess);
	synchronized (entities) {
	    HashSet<Locatable> set = entities.get(clazz);
	    for (Locatable entity : set) {
		if (bounds.contains(entity.getLocation()))
		    objects.add((U) entity);
	    }
	}
	return objects;
    }

    /**
     * Returns a {@code HashSet} of entities with the specific superclass
     * {@code clazz}.
     * 
     * @param clazz
     *            the supertype of the entities
     * @return a {@code HashSet} of the entities
     */
    @SuppressWarnings("unchecked")
    public <U extends Locatable> Iterable<U> getAll(Class<U> clazz) {
	return (Iterable<U>) entities.getOrDefault(clazz, new HashSet<>());
    }

    public EntityList<Persona> getPersonas() {
	return personas;
    }

    public EntityList<NPC> getNPCs() {
	return npcs;
    }

    public Persona getPersona(int index) {
	return this.personas.get(index);
    }

    /**
     * Returns the width, in tiles, of this {@code WorldMap}.
     * 
     * @return the width
     */
    public int getWidth() {
	return width;
    }

    /**
     * Returns the height, in tiles, of this {@code WorldMap}.
     * 
     * @return the height
     */
    public int getHeight() {
	return height;
    }
}
