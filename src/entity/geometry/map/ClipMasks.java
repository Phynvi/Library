package entity.geometry.map;

/**
 * 
 * @author netherfoam
 */
public class ClipMasks {

	public static final int WALL_NORTH = 0x2;
	public static final int WALL_SOUTH = 0x20;
	public static final int WALL_EAST = 0x8;
	public static final int WALL_WEST = 0x80;
	public static final int WALL_NORTH_WEST = 0x1;
	public static final int WALL_SOUTH_EAST = 0x10;
	public static final int WALL_NORTH_EAST = 0x4;
	public static final int WALL_SOUTH_WEST = 0x40;
	public static final int WALL_ALL = WALL_NORTH | WALL_SOUTH | WALL_EAST | WALL_WEST | WALL_NORTH_WEST | WALL_SOUTH_EAST | WALL_NORTH_EAST | WALL_SOUTH_WEST;
	public static final int OBJECT_TILE = 0x100;
	public static final int BLOCKED_TILE = 0x200000;
	public static final int UNLOADED_TILE = 0x80000;
	public static final int OBJECT_BLOCK = 0x20000;

	public static final int OBJECT_ALLOW_RANGE = 0x40000000;
	public static final int BLOCKED_NORTH = WALL_NORTH << 9;
	public static final int BLOCKED_SOUTH = WALL_SOUTH << 9;
	public static final int BLOCKED_EAST = WALL_EAST << 9;
	public static final int BLOCKED_WEST = WALL_WEST << 9;
	public static final int BLOCKED_NORTH_WEST = WALL_NORTH_WEST << 9;
	public static final int BLOCKED_NORTH_EAST = WALL_NORTH_EAST << 9;
	public static final int BLOCKED_SOUTH_EAST = WALL_SOUTH_EAST << 9;
	public static final int BLOCKED_SOUTH_WEST = WALL_SOUTH_WEST << 9;
	public static final int BLOCKED_ALL = BLOCKED_NORTH | BLOCKED_SOUTH | BLOCKED_EAST | BLOCKED_WEST | BLOCKED_NORTH_WEST | BLOCKED_NORTH_EAST | BLOCKED_SOUTH_EAST | BLOCKED_SOUTH_WEST;
	public static final int WALL_ALLOW_RANGE_NORTH = WALL_NORTH << 22;
	public static final int WALL_ALLOW_RANGE_SOUTH = WALL_SOUTH << 22;
	public static final int WALL_ALLOW_RANGE_WEST = WALL_WEST << 22;
	public static final int WALL_ALLOW_RANGE_EAST = WALL_EAST << 22;
	public static final int WALL_ALLOW_RANGE_NORTH_WEST = WALL_NORTH_WEST << 22;
	public static final int WALL_ALLOW_RANGE_NORTH_EAST = WALL_NORTH_EAST << 22;
	public static final int WALL_ALLOW_RANGE_SOUTH_EAST = WALL_SOUTH_EAST << 22;
	public static final int WALL_ALLOW_RANGE_SOUTH_WEST = WALL_SOUTH_WEST << 22;
	public static final int WALL_ALLOW_RANGE_ALL = WALL_ALLOW_RANGE_NORTH | WALL_ALLOW_RANGE_SOUTH | WALL_ALLOW_RANGE_EAST | WALL_ALLOW_RANGE_WEST | WALL_ALLOW_RANGE_NORTH_WEST | WALL_ALLOW_RANGE_NORTH_EAST | WALL_ALLOW_RANGE_SOUTH_EAST | WALL_ALLOW_RANGE_SOUTH_WEST;

	public static final int DECORATION_BLOCK = 0x40000;

	private ClipMasks() {
		//Private constructor
	}
}
