package entity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * The {@code EntityList} class holds an array of {@code Entity} types. The {@code EntityList} class
 * works similarly to the {@link java.util.ArrayList} class except with specific {@code Entity}
 * functions.
 * 
 * @author Albert Beaupre
 * 
 * @param <E>
 *            The {@code Entity} type
 */
public class EntityList<E extends Entity> implements Iterable<E> {

	private Entity[] data;
	private int size;

	/**
	 * Constructs a new {@code EntityList} with a specified {@code initialCapacity}.
	 * 
	 * @param initalCapacity
	 *            the starting capacity of the data array in this {@code EntityList}
	 */
	public EntityList(int initialCapacity) {
		this.data = new Entity[initialCapacity];
	}

	private int openIndex() {
		for (int i = 1; i < data.length; i++)
			if (data[i] == null)
				return i;
		return -1;
	}

	/**
	 * Adds the specified {@code Entity} type to this {@code EntityList} and sets its index value to the
	 * available index in this {@code EntityList} that has not been used and returns {@code true} if the
	 * {@code Entity} was added; otherwise {@code false} is returned;
	 * 
	 * @param entity
	 *            the {@code Entity} type to add
	 * @return true if the {@code Entity} was added; return false otherwise
	 * 
	 * @see entity.Entity#getIndex()
	 */
	public boolean add(E entity) {
		int index = openIndex();
		if (index != -1) {
			data[index] = entity;
			entity.setIndex(index);
			size++;
			return true;
		}
		return false;
	}

	/**
	 * Removes the specified {@code Entity} from this {@code EntityList} and returns {@code true} if the
	 * {@code Entity} was removed; otherwise {@code false} is returned.
	 * 
	 * @param entity
	 *            the entity to remove
	 * @return true if the {@code Entity} was removed; return false otherwise
	 */
	public boolean remove(E entity) {
		int index = entity.getIndex();
		if (index < 0)
			return false;
		if (data[index] == entity) {
			data[index] = null;
			size--;
			return true;
		}
		return false;
	}

	/**
	 * Removes the {@code Entity} at the specified {@code index} of this {@code EntityList}.
	 * 
	 * @param index
	 *            the index to remove the {@code Entity} at
	 * @return true if the {@code Entity} was removed
	 * 
	 * @see #remove(Entity)
	 */
	public boolean remove(int index) {
		if (index < 0 || data[index] == null)
			return false;
		data[index] = null;
		size--;
		return true;
	}

	/**
	 * Returns the {@code Entity} placed at the specified {@code index} of this {@code EntityList}.
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
	 * Clears this {@code EntityList} of every contained {@code Entity}.
	 */
	public void clear() {
		int length = data.length;
		data = new Entity[length];
		size = 0;
	}

	/**
	 * Returns the amount of {@code Entity} types within this {@code EntityList} .
	 * 
	 * @return the amount of entities
	 */
	public int size() {
		return size;
	}

	/**
	 * Returns true if this {@code EntityList} doesn't contain any {@code Entity}.
	 * 
	 * @return true if empty; return false otherwise
	 */
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * Checks if the specified {@code Object} is within this {@code EntityList}, and if so, returns
	 * {@code true}, {@code false} is returned otherwise. The argument {@code o} must be of type
	 * {@code Entity}, otherwise it will return {@code false} no matter what.
	 * 
	 * @param o
	 *            the object to check
	 * @return true if the object is within this {@code EntityList}; return false otherwise
	 */
	public boolean contains(Object o) {
		if (!(o instanceof Entity))
			return false;
		Entity e = (Entity) o;
		if (e.getIndex() < 0 || e.getIndex() >= data.length)
			return false;
		return data[e.getIndex()] != null && data[e.getIndex()].equals(o);
	}

	/**
	 * Creates a new {@code Iterator} for this {@code EntityList}.
	 */
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
	 * Returns a <b>copy</b> of the {@code Entity} data array in this {@code EntityList}.
	 * 
	 * @return a copy of the data
	 */
	public Entity[] toArray() {
		Entity[] entities = new Entity[size];
		for (int i = 0; i < entities.length; i++)
			entities[i] = data[i + 1];
		return entities;
	}

	@SuppressWarnings("unchecked")
	public List<E> toList() {
		ArrayList<E> list = new ArrayList<>(data.length);
		for (Entity e : data)
			if (e != null)
				list.add((E) e);
		return list;
	}

}
