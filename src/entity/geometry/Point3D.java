package entity.geometry;

import java.util.Objects;
import util.configuration.ConfigSection;
import util.configuration.YMLSerializable;

/**
 * A 3-dimensional point representing a location in {@code (x,y,z)} coordinate space, specified in
 * integer precision.
 * 
 * @author Albert Beaupre
 */
public class Point3D implements YMLSerializable {

	/**
	 * The X coordinate of this {@code Point3D}. If no X coordinate is set it will default to 0.
	 */
	public int x;

	/**
	 * The Y coordinate of this {@code Point3D}. If no Y coordinate is set it will default to 0.
	 */
	public int y;

	/**
	 * The Z coordinate of this {@code Point3D}. If no Z coordinate is set it will default to 0.
	 */
	public int z;

	public Point3D(Point3D point) {
		this(point.x, point.y, point.z);
	}

	/**
	 * Constructs and initializes a point at the specified {@code (x,y,z)} location in the coordinate
	 * space.
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
	 * Returns {@code true} if the specified {@code Point3D} has a distance <= the specified
	 * {@code range} argument. If it does not, then {@code false} is returned.
	 * 
	 * @param point
	 *            the point to check if this {@code Point3D} is in range
	 * @param range
	 *            the range distance to check
	 * @return true if within range; return false otherwise
	 */
	public boolean inRange(Point3D point, int range) {
		return point.distance(this) <= range;
	}

	/**
	 * Returns a new translated {@code Point3D}, at location {@code (x,y,z)}, by {@code dx} along the
	 * {@code x} axis and {@code dy} along the {@code y} axis and {@code dz} along the {@code z} axis so
	 * that it now represents the point {@code (x+dx,y+dy, z+dz)}.
	 *
	 * @param dx
	 *            the distance to move this point along the X axis
	 * @param dy
	 *            the distance to move this point along the Y axis
	 * @param dz
	 *            the distance to move this point along the Z axis
	 * 
	 * @return the {@code Point3D} with the translated coordinates
	 */
	public Point3D translate(int dx, int dy, int dz) {
		return new Point3D(this.x + dx, this.y + dy, this.z + dz);
	}

	/**
	 * Returns the distance between this {@code Point3D} values and the specified {@code Point3D}
	 * coordinate values.
	 * 
	 * @param point
	 *            the {@code Point3D} to compare the coordinates to
	 * 
	 * @return the distance between this point and the specific {@code Point3D}
	 */
	public double distance(Point3D point) {
		double ax = Math.abs(point.x - x);
		double ay = Math.abs(point.y - y);
		double az = Math.abs(point.z - z);
		return Math.sqrt((ax * ax) + (ay * ay) + (az * az));
	}

	/**
	 * Returns the distance between this {@code Point3D} values and the specified {@code (x,y,z)}
	 * coordinate values.
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
	 * Determines whether or not three coordinates are equal. Two instances of {@code Point3D} are equal
	 * if the coordinate values of their {@code code x}, {@code y}, and {@code z} member fields,
	 * representing their position in the coordinate space, are the same.
	 * 
	 * @param obj
	 *            an object to be compared with this {@code Point3D}
	 * 
	 * @return {@code true} if the object compared is an instance of {@code Point3D} and has the same
	 *         values; return {@code false} otherwise
	 */
	public boolean equals(Object obj) {
		if (obj instanceof Point3D) {
			Point3D p = (Point3D) obj;
			return p.x == x && p.y == y && p.z == z;
		}
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see util.yaml.YMLSerializable#serialize()
	 */
	public ConfigSection serialize() {
		ConfigSection config = new ConfigSection();
		config.put("x", x);
		config.put("y", y);
		config.put("z", z);
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see util.yaml.YMLSerializable#deserialize(util.yaml.ConfigSection)
	 */
	public void deserialize(ConfigSection section) {
		this.x = section.getInt("x");
		this.y = section.getInt("y");
		this.z = section.getInt("z");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return String.format("Point3D[x=%s, y=%s, z=%s]", x, y, z);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return Objects.hash(x, y, z);
	}

}
