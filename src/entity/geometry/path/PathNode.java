package entity.geometry.path;

import java.util.ArrayList;
import java.util.List;
import entity.geometry.Point3D;

/**
 * 
 * @author Albert Beaupre
 */
public class PathNode extends Point3D {

	public PathNode(int x, int y, int z) {
		super(x, y, z);
	}

	/**
	 * 
	 */
	public List<PathNode> neighbors(int radius) {
		List<PathNode> list = new ArrayList<>(radius * radius * radius);
		for (int nx = -radius; nx <= radius; nx++) {
			for (int ny = -radius; ny <= radius; ny++) {
				if (nx == 0 && ny == 0)
					continue;
				list.add(new PathNode(nx, ny, z));
			}
		}
		return list;
	}

}
