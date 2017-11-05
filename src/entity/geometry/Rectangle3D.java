package entity.geometry;

import java.util.ArrayList;
import java.util.List;

public class Rectangle3D implements Shape3D {

	public final int x, y, z;
	public final int length, width, height;

	public Rectangle3D(int x, int y, int z, int width, int height, int depth) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.length = width;
		this.width = height;
		this.height = depth;
	}

	@Override
	public boolean contains(Point3D point) {
		if (point.x < x || point.x > x + length)
			return false;
		if (point.y < y || point.y > y + width)
			return false;
		if (point.z < z || point.z > z + height)
			return false;
		return true;
	}

	@Override
	public List<Point3D> listPoints() {
		List<Point3D> list = new ArrayList<>(length * width * height);
		for (int i = x - 1; i < x + length; i++) {
			for (int j = y - 1; j < y + width; j++) {
				for (int k = z - 1; k < z + height; k++) {
					list.add(new Point3D(i + 1, j + 1, k + 1));
				}
			}
		}
		return list;
	}
}
