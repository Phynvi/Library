package geometry;

/**
 * A 3-dimensional point representing a location in {@code (x,y,z)} coordinate
 * space, specified in integer precision.
 * 
 * @author Albert Beaupre
 */
public class Point3D {

    /**
     * The X coordinate of this {@code Point3D}. If no X coordinate is set it
     * will default to 0.
     */
    public int x;

    /**
     * The Y coordinate of this {@code Point3D}. If no Y coordinate is set it
     * will default to 0.
     */
    public int y;

    /**
     * The Z coordinate of this {@code Point3D}. If no Z coordinate is set it
     * will default to 0.
     */
    public int z;

    /**
     * Constructs and initializes a point at the origin (0, 0, 0) of the
     * coordinate space.
     */
    public Point3D() {
	this(0, 0, 0);
    }

    /**
     * Constructs and initializes a point at the specified {@code (x,y,z)}
     * location in the coordinate space.
     * 
     * @param x
     *            the X coordinate of the newly constructed {@code Point}
     * @param y
     *            the Y coordinate of the newly constructed {@code Point}
     * @param z
     *            the Z coordinate of the newly constructed {@code Point}
     */
    public Point3D(int x, int y, int z) {
	this.x = x;
	this.y = y;
	this.z = z;
    }

    /**
     * Translates this point, at location {@code (x,y)}, by {@code dx} along the
     * {@code x} axis and {@code dy} along the {@code y} axis so that it now
     * represents the point {@code (x+dx,y+dy)}.
     *
     * @param dx
     *            the distance to move this point along the X axis
     * @param dy
     *            the distance to move this point along the Y axis
     * @param dz
     *            the distance to move this point along the Z axis
     */
    public void translate(int dx, int dy, int dz) {
	this.x += dx;
	this.y += dy;
	this.z += dz;
    }

    /**
     * Returns the distance between this {@code Point3D} values and the
     * specified {@code Point3D} coordinate values.
     * 
     * @param point
     *            the {@code Point3D} to compare the coordinates to
     * 
     * @return the distance between this point and the specific {@code Point3D}
     */
    public double distance(Point3D point) {
	int ax = Math.abs(point.x - x);
	int ay = Math.abs(point.y - y);
	int az = Math.abs(point.z - z);
	return Math.sqrt(ax + ay + az);
    }

    /**
     * Returns the distance between this {@code Point3D} values and the
     * specified {@code (x,y,z)} coordinate values.
     * 
     * @param x
     *            the X coordinate of the coordinates to compare
     * @param y
     *            the Y coordinate of the coordinates to compare
     * @param z
     *            the Z coordinate of the coordinates to compare
     * 
     * @return the distance between this point and the specific coordinates
     */
    public double distance(int x, int y, int z) {
	return distance(new Point3D(x, y, z));
    }

    /**
     * Determines whether or not two points are equal. Two instances of
     * {@code Point3D} are equal if the values of their {@code code}, {@code y},
     * and {@code z} member fields, representing their position in the
     * coordinate space, are the same.
     * 
     * @param obj
     *            an object to be compared with this {@code Point3D}
     * 
     * @return {@code true} if the object compared is an instance of
     *         {@code Point3D} and has the same values; return {@code false}
     *         otherwise
     */
    public boolean equals(Object obj) {
	if (obj instanceof Point3D) {
	    Point3D p = (Point3D) obj;
	    return p.x == x && p.y == y && p.z == z;
	}
	return super.equals(obj);
    }
}
