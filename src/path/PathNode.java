package path;

import java.awt.Point;

/**
 * 
 * @author Albert Beaupre
 */
public class PathNode {

	public Point point;

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public PathNode(int x, int y) {
		this(new Point(x, y));
	}

	/**
	 * 
	 * @param point
	 */
	public PathNode(Point point) {
		this.point = point;
	}

}
