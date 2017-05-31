package entity.geometry.area;

import entity.Entity;
import entity.event.EntityLocationChangeEvent;
import entity.geometry.Location;
import entity.geometry.Point3D;
import entity.geometry.Shape3D;
import event.EventListener;
import event.EventMethod;

/**
 * The {@code Area} class is used to handle a specific area based on the bounds
 * of a {@code Shape3D} object. The {@code Area} class is used by the
 * {@link entity.geometry.area.AreaManager} class to manage how the areas are
 * handled.
 * 
 * @author Albert Beaupre
 * 
 * @see entity.geometry.area.AreaManager
 */
public abstract class Area implements Shape3D, EventListener {

    private final Shape3D bounds;

    /**
     * Constructs a new {@code Area} with the bounding area based on the
     * specified {@code bounds} argument.
     */
    public Area(Shape3D bounds) {
	this.bounds = bounds;
    }

    /**
     * This method is called when the specified {@code Entity} has entered this
     * {@code Area} by the specified {@code AreaChangeType}.
     * 
     * @param entity
     *            the entity entering this {@code Area}
     * @param type
     *            the way the {@code Entity} has entered this {@code Area}
     */
    public abstract void onEnter(Entity entity, AreaChangeType type);

    /**
     * This method is called when the specified {@code Entity} has left this
     * {@code Area} by the specified {@code AreaChangeType}.
     * 
     * @param entity
     *            the entity leaving this {@code Area}
     * @param type
     *            the way the {@code Entity} has left this {@code Area}
     */
    public abstract void onLeave(Entity entity, AreaChangeType type);

    /*
     * (non-Javadoc)
     * 
     * @see geometry.Shape3D#contains(geometry.Point3D)
     */
    public boolean contains(Point3D point) {
	return bounds.contains(point);
    }

    @EventMethod
    private void onAreaChange(EntityLocationChangeEvent event) {
	Location current = event.getEntity().getLocation();
	Location previous = event.getEntity().getPreviousLocation();
	if (previous.equals(current))
	    return;

	if (contains(current) && !contains(previous)) {
	    onEnter(event.getEntity(), event.getType());
	} else if (!contains(current) && contains(previous)) {
	    onLeave(event.getEntity(), event.getType());
	}
    }
}
