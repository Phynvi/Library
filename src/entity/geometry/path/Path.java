package entity.geometry.path;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Albert Beaupre
 */
public class Path {

	private List<Direction> directions;
	private boolean failed;

	/**
	 * Creates a path from the given array of specified {@code directions}.
	 * 
	 * @param directions
	 *            the directions to create a path from
	 */
	public Path(Direction... directions) {
		this.directions = new ArrayList<>();
		for (Direction dir : directions) this.directions.add(dir);
	}

	/**
	 * Creates a path from the given list of specified {@code directions}.
	 * 
	 * @param directions
	 *            the direction to create the path from
	 */
	public Path(List<Direction> directions) {
		this.directions = directions;
	}

	public void addFirst(Direction dir) {
		this.directions.add(dir);
	}

	/**
	 * Returns the next {@code Direction} of this {@code Path} and removes it from the path.
	 * 
	 * @return the next {@code Direction} of the path
	 */
	public Direction next() {
		return directions.remove(directions.size() - 1);
	}

	/**
	 * Returns the next {@code Direction} of this {@code Path} and removes it from the path.
	 * 
	 * @return the next {@code Direction} of the path
	 */
	public Direction peek() {
		return directions.get(directions.size() - 1);
	}

	/**
	 * A non-functional setter for the return result of {@link Path#hasFailed()} .
	 *
	 * @param fail
	 *            whether the path failed or not.
	 */
	public void setFailed(boolean fail) {
		this.failed = fail;
	}

	/**
	 * Returns true if this path will never reach its intended destination. Even if this returns true,
	 * then the path may still contain some valid information on how to reach the target position.
	 *
	 * @return true if the path failed, false if it didn't.
	 */
	public boolean hasFailed() {
		return failed;
	}

	/**
	 * Returns true if this {@code Path} has no directions to walk; return false otherwise.
	 * 
	 * @return true if empty; otherwise return false
	 */
	public boolean isEmpty() {
		return directions.isEmpty();
	}

}
