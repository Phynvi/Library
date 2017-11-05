package path;

import entity.geometry.Location;

public interface PathFinder {

	public Path findPath(Location start, Location goal, int sizeX, int sizeY);

	public boolean blocked(Location from, Location to, int sizeX, int sizeY);

}
