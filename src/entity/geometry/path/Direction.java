package entity.geometry.path;

import entity.geometry.Location;

/**
 * @author netherfoam
 */
public abstract class Direction {

	/**
	 * Delta 'X' value of this {@code Direction}.
	 */
	public final int dx;

	/**
	 * Delta 'Y' value of this {@code Direction}.
	 */
	public final int dy;

	public final int clipTo;
	public final int clipFrom;

	/**
	 * 
	 * @param dx
	 * @param dy
	 * @param clipTo
	 * @param clipFrom
	 */
	protected Direction(int dx, int dy, int clipTo, int clipFrom) {
		this.dx = dx;
		this.dy = dy;
		this.clipTo = clipTo;
		this.clipFrom = clipFrom;
	}

	/**
	 * Returns true if you can walk this direction, from the given location. Eg, does this tile let
	 * you walk north?
	 * 
	 * @param from
	 *           the location starting from
	 * @return true if a single tile entity can move north, false
	 */
	public final boolean canWalk(Location from) {
		return conflict(from) == 0;
	}

	/**
	 * 
	 * @param from
	 * @return
	 */
	public final int conflict(Location from) {
		return conflictTo(from) + conflictFrom(from);
	}

	/**
	 * 
	 * @param from
	 * @return
	 */
	public abstract int conflictFrom(Location from);

	/**
	 * 
	 * @param to
	 * @return
	 */
	public abstract int conflictTo(Location to);

	/**
	 * Returns the walk mask of this {@code Direction}.
	 * 
	 * @return the walk mask
	 */
	public abstract int getWalkMask();

	/**
	 * Returns true if you can shoot in this direction, from the given location. Eg, does this tile
	 * let you shoot north?
	 * 
	 * @param from
	 *           the location starting from
	 * @return true if a single tile entity can shoot north, false
	 */
	public abstract boolean canShoot(Location from);
}