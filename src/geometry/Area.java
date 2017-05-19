package geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents an area combined by multiple different shapes.
 * 
 * @author Albert Beaupre
 * 
 * @see geometry.Shape3D
 */
public class Area implements Shape3D {

    private HashMap<Class<?>, HashSet<Locatable>> entities;
    private ArrayList<Shape3D> combinedArea;

    /**
     * Constructs a new {@code Area} with no entities or shapes to create the
     * area.
     */
    public Area() {
	this.entities = new HashMap<>();
	this.combinedArea = new ArrayList<>();
    }

    /**
     * Combines the specified {@code Shape3D} to this {@code Area} to create a
     * larger area based on the given shape.
     * 
     * @param shape
     *            the {@code Shape3D} to combine to this {@code Area}
     */
    public void combine(Shape3D shape) {
	this.combinedArea.add(shape);
    }

    /**
     * Puts the specified {@code entity} within this {@code Area} allowing it to
     * be searched for or add/removed from this {@code Area}.
     * 
     * @param entity
     *            the entity to put in this {@code Area}
     * 
     * @throws NullPointerException
     *             if there is no area define in this {@code Area} class where
     *             the entity is located
     */
    public void put(Locatable entity) {
	synchronized (entities) {
	    if (!this.contains(entity.getLocation()))
		throw new NullPointerException("There is no area defined where the specified entity is located");
	    HashSet<Locatable> set = entities.getOrDefault(entity.getClass(), new HashSet<>());
	    set.add(entity);
	    entities.put(entities.getClass(), set);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see geometry.Shape3D#contains(geometry.Point3D)
     */
    public boolean contains(Point3D point) {
	for (Shape3D shape : combinedArea)
	    if (shape.contains(point))
		return true;
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see geometry.Shape3D#intersects(geometry.Shape3D)
     */
    public boolean intersects(Shape3D shape) {
	for (Shape3D s : combinedArea)
	    if (s.intersects(shape))
		return true;
	return false;
    }

    /**
     * Finds any entities in this {@code Area} by the specified {@code bounds}
     * that are an instance of the specified {@code clazz} and uses the
     * {@code guess} value to guess how many entities will approximately be
     * found within this {@code Area}.
     * 
     * @param bounds
     *            the {@code Shape3D} bounds to search in this {@code Area}
     * @param guess
     *            the guess of how many entities will be found
     * @param clazz
     *            the class type of the entity to be found
     * 
     * @return a {@link java.util.HashSet} filled with the entities found within
     *         the {@code bounds}
     */
    @SuppressWarnings("unchecked")
    public <U> HashSet<U> findEntities(Shape3D bounds, int guess, Class<U> clazz) {
	HashSet<U> objects = new HashSet<>(guess);
	synchronized (entities) {
	    HashSet<Locatable> set = entities.get(clazz);
	    for (Locatable entity : set) {
		if (bounds.contains(entity.getLocation()))
		    objects.add((U) entity);
	    }
	}
	return objects;
    }
}
