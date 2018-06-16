package entity.geometry.path;

import entity.geometry.Point3D;

/**
 * 
 * @author Albert Beaupre
 */
public interface PathFinder {

	/**
	 * 
	 * @param start
	 * @param goal
	 * @param sizeX
	 * @param sizeY
	 * @return
	 */
	public Path findPath(Point3D start, Point3D goal, int sizeX, int sizeY);

	/**
	 * 
	 * @param from
	 * @param to
	 * @param sizeX
	 * @param sizeY
	 * @return
	 */
	public boolean blocked(Point3D from, Point3D to, int sizeX, int sizeY);

}
