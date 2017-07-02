package path.astar;

import entity.geometry.Location;
import path.Path;
import path.PathFinder;

public class AStarPathFinder implements PathFinder {

    @Override
    public Path findPath(Location start, Location goal, int sizeX, int sizeY) {
	return null;
    }

    @Override
    public boolean blocked(Location from, Location to, int sizeX, int sizeY) {

	return false;
    }

}
