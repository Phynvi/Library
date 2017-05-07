package geometry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Represents an area combined by multiple different shapes.
 * 
 * @see geometry.Shape3D
 * 
 * @author Albert Beaupre
 */
public class Area<T extends Point3D> implements Shape3D {

    private HashMap<Class<?>, HashSet<T>> entities;
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
     */
    public void put(T entity) {
	synchronized (entities) {
	    HashSet<T> set = entities.get(entity.getClass());
	    set.add(entity);
	    entities.put(entities.getClass(), set);
	}
    }

    @Override
    public boolean contains(Point3D point) {
	for (Shape3D shape : combinedArea)
	    if (shape.contains(point))
		return true;
	return false;
    }

    @Override
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
    public <U extends T> HashSet<U> findEntities(Shape3D bounds, int guess, Class<U> clazz) {
	HashSet<U> objects = new HashSet<>(guess);
	synchronized (entities) {
	    HashSet<T> set = entities.get(clazz);
	    for (T entity : set) {
		if (bounds.contains(entity))
		    objects.add((U) entity);
	    }
	}
	return objects;
    }
}
