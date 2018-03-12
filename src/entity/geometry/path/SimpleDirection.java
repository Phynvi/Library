package entity.geometry.path;

import entity.geometry.Location;
import entity.geometry.map.ClipMasks;

/**
 * @author Albert Beaupre
 */
public class SimpleDirection extends Direction {

	/**
	 * 
	 * @param dx
	 * @param dy
	 * @param clipTo
	 * @param clipFrom
	 */
	protected SimpleDirection(int dx, int dy, int clipTo, int clipFrom) {
		super(dx, dy, clipTo, clipFrom);
	}

	@Override
	public int conflictTo(Location from) {
		return (from.map.getClip(from.x + dx, from.y + dy, from.z) & clipTo);
	}

	@Override
	public int conflictFrom(Location from) {
		return (from.map.getClip(from.x, from.y, from.z) & clipFrom);
	}

	@Override
	public boolean canShoot(Location from) {
		if (conflict(from) == 0)
			return true;
		
		int to = from.map.getClip(from.x + dx, from.y + dy, from.z) & clipTo;
		int rangeFlags = (to & ClipMasks.WALL_ALLOW_RANGE_ALL) >> 22;
		to = to & ~(rangeFlags);
		to = to & ~(rangeFlags << 9);
		return (to & this.clipTo) == 0;
	}

	@Override
	public String toString() {
		return "(" + dx + "," + dy + ")";
	}

	@Override
	public int getWalkMask() {
		return clipTo;
	}
}