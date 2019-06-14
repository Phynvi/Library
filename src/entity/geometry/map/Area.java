package entity.geometry.map;

import java.util.List;
import entity.Entity;
import entity.geometry.Location;
import entity.geometry.Point3D;
import entity.geometry.Shape3D;
import event.EventListener;
import event.EventMethod;
import event.impl.EntityLocationChangeEvent;

/**
 * The {@code Area} class is used to handle a specific area based on the bounds of a {@code Shape3D}
 * object. The {@code Area} class is used by the {@link entity.geometry.map.AreaManager} class to
 * manage how the areas are handled.
 * 
 * @author Albert Beaupre
 * 
 * @see entity.geometry.map.AreaManager
 */
public abstract class Area implements Shape3D, EventListener {

	private final Shape3D bounds;

	/**
	 * Constructs a new {@code Area} with the bounding area based on the specified {@code bounds}
	 * argument.
	 */
	public Area(Shape3D bounds) {
		this.bounds = bounds;
	}

	/**
	 * This method is called when the specified {@code Entity} has entered this {@code Area} by the
	 * specified {@code AreaChangeType}.
	 * 
	 * @param entity
	 *            the entity entering this {@code Area}
	 * @param type
	 *            the way the {@code Entity} has entered this {@code Area}
	 */
	public abstract void onEnter(Entity entity, AreaChangeType type);

	/**
	 * This method is called when the specified {@code Entity} has left this {@code Area} by the
	 * specified {@code AreaChangeType}.
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
	 * @see entity.geometry.Shape3D#listPoints()
	 */
	public List<Point3D> listPoints() {
		return bounds.listPoints();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see geometry.Shape3D#contains(geometry.Point3D)
	 */
	public boolean contains(Point3D point) {
		return bounds.contains(point);
	}

	@EventMethod
	public void onAreaChange(EntityLocationChangeEvent event) {
		Location current = event.getCurrentLocation();
		Location previous = event.getPreviousLocation();

		if (previous == null) {
			if (contains(current)) {
				onEnter(event.getEntity(), event.getType());
			}
			return;
		}

		if (contains(current) && !contains(previous)) {
			onEnter(event.getEntity(), event.getType());
		} else if (!contains(current) && contains(previous)) {
			onLeave(event.getEntity(), event.getType());
		}
	}

	@Override
	public boolean inRange(Point3D point, int range) {
		return bounds.inRange(point, range);
	}
}
