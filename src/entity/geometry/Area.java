package entity.geometry;

import event.EventListener;

/**
 * The {@code Area} class is used to handle a specific area based on the bounds
 * of a {@code Shape3D} object. The {@code Area} class is used by the
 * {@link entity.geometry.AreaManager} class to manage how the areas are
 * handled.
 * 
 * @author Albert Beaupre
 * 
 * @see entity.geometry.AreaManager
 */
public class Area implements Shape3D, EventListener {

    private final Shape3D bounds;

    /**
     * 
     * Constructs a new {@code Area} with the bounding area based on the
     * specified {@code bounds} argument.
     */
    public Area(Shape3D bounds) {
	this.bounds = bounds;
    }

    /*
     * (non-Javadoc)
     * 
     * @see geometry.Shape3D#contains(geometry.Point3D)
     */
    public boolean contains(Point3D point) {
	return bounds.contains(point);
    }

    /*
     * (non-Javadoc)
     * 
     * @see geometry.Shape3D#intersects(geometry.Shape3D)
     */
    public boolean intersects(Shape3D shape) {
	return bounds.intersects(shape);
    }

}
