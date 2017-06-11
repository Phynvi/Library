package entity;

import java.util.Arrays;
import java.util.Iterator;

/**
 * The {@code EntityList} class holds an array of {@code Entity} types. The
 * {@code EntityList} class works similarly to the {@link java.util.ArrayList}
 * class except with specific {@code Entity} functions.
 * 
 * @author Albert Beaupre
 * 
 * @param <E>
 *            The {@code Entity} type
 */
public class EntityList<E extends Entity> implements Iterable<E> {

    private static final int DEFAULT_CAPACITY = 1000;
    private static final float DEFAULT_LOAD_FACTOR = 0.25f;

    private Entity[] data;
    private float loadFactor;
    private int size;
    private int lastOpenIndex = -1;

    /**
     * Constructs a new {@code EntityList} with a default capacity of 1000.
     */
    public EntityList() {
	this.data = new Entity[DEFAULT_CAPACITY];
	this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    /**
     * Constructs a new {@code EntityList} with a specified
     * {@code initialCapacity}.
     * 
     * @param initalCapacity
     *            the starting capacity of the data array in this
     *            {@code EntityList}
     */
    public EntityList(int initialCapacity) {
	this.data = new Entity[initialCapacity];
	this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    /**
     * Ensures that the size of data within this {@code EntityList} is relative
     * to the capacity of this {@code EntityList}.
     */
    private void ensureCapacity() {
	if (size >= data.length * loadFactor)
	    this.data = Arrays.copyOf(data, (int) (data.length + (data.length * loadFactor)));
    }

    /**
     * Adds the specified {@code Entity} type to this {@code EntityList} and
     * sets its index value to the available index in this {@code EntityList}
     * that has not been used and returns {@code true} if the {@code Entity} was
     * added; otherwise {@code false} is returned;
     * 
     * @param entity
     *            the {@code Entity} type to add
     * @return true if the {@code Entity} was added; return false otherwise
     * 
     * @see entity.Entity#getIndex()
     */
    public boolean add(E entity) {
	ensureCapacity();
	if (lastOpenIndex != -1) {
	    if (data[lastOpenIndex] == null)
		size++;
	    data[lastOpenIndex] = entity;
	    entity.setIndex(lastOpenIndex + 1);
	    lastOpenIndex = -1;
	} else {
	    size++;
	    data[size] = entity;
	    entity.setIndex(size);
	}
	return false;
    }

    /**
     * Removes the specified {@code Entity} from this {@code EntityList}, sets
     * the index of the {@code Entity} to -1, and returns {@code true} if the
     * {@code Entity} was removed; otherwise {@code false} is returned.
     * 
     * @param entity
     *            the entity to remove
     * @return true if the {@code Entity} was removed; return false otherwise
     */
    public boolean remove(E entity) {
	if (entity == null || entity.getIndex() == -1 || entity.getIndex() >= data.length)
	    return false;
	this.lastOpenIndex = entity.getIndex();
	data[entity.getIndex()] = null;
	entity.setIndex(-1);
	size--;
	return true;
    }

    /**
     * Removes the {@code Entity} at the specified {@code index} of this
     * {@code EntityList}.
     * 
     * @param index
     *            the index to remove the {@code Entity} at
     * @return true if the {@code Entity} was removed
     * 
     * @see #remove(Entity)
     */
    @SuppressWarnings("unchecked")
    public boolean remove(int index) {
	return remove((E) data[index]);
    }

    /**
     * Returns the {@code Entity} placed at the specified {@code index} of this
     * {@code EntityList}.
     * 
     * @param index
     *            the index to retrieve the {@code Entity} at
     * @return the {@code Entity} at the index; return null if non-existent
     */
    @SuppressWarnings("unchecked")
    public E get(int index) {
	return (E) data[index];
    }

    /**
     * Clears this {@code EntityList} of every contained {@code Entity}, sets
     * the index of every {@code Entity} to -1.
     */
    public void clear() {
	for (Entity e : this.data) {
	    e.setIndex(-1);
	}
	int length = data.length;
	lastOpenIndex = -1;
	data = new Entity[length];
	size = 0;
    }

    /**
     * Returns the amount of {@code Entity} types within this {@code EntityList}
     * .
     * 
     * @return the amount of entities
     */
    public int size() {
	return size;
    }

    /**
     * Returns true if this {@code EntityList} doesn't contain any
     * {@code Entity}.
     * 
     * @return true if empty; return false otherwise
     */
    public boolean isEmpty() {
	return size == 0;
    }

    /**
     * Checks if the specified {@code Object} is within this {@code EntityList},
     * and if so, returns {@code true}, {@code false} is returned otherwise. The
     * argument {@code o} must be of type {@code Entity}, otherwise it will
     * return {@code false} no matter what.
     * 
     * @param o
     *            the object to check
     * @return true if the object is within this {@code EntityList}; return
     *         false otherwise
     */
    public boolean contains(Object o) {
	if (!(o instanceof Entity))
	    return false;
	Entity index = (Entity) o;
	if (index.getIndex() < 0 || index.getIndex() >= data.length)
	    return false;
	return data[index.getIndex()] != null;
    }

    public Iterator<E> iterator() {
	return new Iterator<E>() {
	    private int index;

	    @Override
	    public boolean hasNext() {
		return index < data.length;
	    }

	    @SuppressWarnings("unchecked")
	    @Override
	    public E next() {
		return (E) data[index++];
	    }

	    @SuppressWarnings("unchecked")
	    @Override
	    public void remove() {
		EntityList.this.remove((E) data[index]);
	    }
	};
    }

    /**
     * Returns a <b>copy</b> of the {@code Entity} data array in this
     * {@code EntityList}.
     * 
     * @return a copy of the data
     */
    public Entity[] toArray() {
	return Arrays.copyOf(data, data.length);
    }

}
