package container;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import util.configuration.ConfigSection;
import util.configuration.YMLSerializable;

/**
 * A {@code Container} has a specific number of slots (based on the capacity of the
 * {@code Container)} which are filled by {@code Item} elements. Any slot not used within this
 * {@code Container} has a default value of null but is still usable and is represented as an
 * 'empty' slot.
 * 
 * <p>
 * The {@code Container} class implements {@link java.util.Collection} for easier references to
 * collection classes and methods. The {@code Container} class also implements
 * {@link java.lang.Iterable} for much easier iterations, especially through the {@code Item} data
 * of this {@code Container}.
 * 
 * @author Albert Beaupre
 *
 * @param <E>
 *            The type of {@code Item} used within this collection
 * 
 * @see container.Item
 */
@SuppressWarnings("unchecked")
public class Container<E extends Item> implements Collection<E>, Iterable<E>, YMLSerializable {

	private final ContainerHandler<E> handler;
	private final int capacity; // Containers are not meant to have a large
	// capacity
	private Item[] data; // The items within this container

	private final int maximumStack; // The maximum amount of an item before it
									// cannot be increase in size anymore
	private final int minimumStack; // The minimum amount of an item before it is
									// destroyed

	public ArrayList<Consumer<Container<E>>> refreshers;

	/**
	 * Constructs a new {@code Container} from the specified arguments.
	 * 
	 * @param handler
	 *            the container's handler
	 * @param capacity
	 *            the maximum amount of items allowed in this container
	 * @param minStack
	 *            the minimum stack an item can be stacked to
	 * @param maxStack
	 *            the maximum stack an itme can be stacked to
	 */
	public Container(ContainerHandler<E> handler, int capacity, int minimumStack, int maximumStack) {
		this.data = new Item[this.capacity = capacity];
		this.handler = handler;
		this.minimumStack = minimumStack;
		this.maximumStack = maximumStack;
		this.refreshers = new ArrayList<>();
	}

	/**
	 * Used for the iteration of this {@code Container}.
	 * 
	 * @author Albert Beaupre
	 */
	private class ContainerIterator implements Iterator<E> {

		private int index;

		public boolean hasNext() {
			return index < capacity;
		}

		public E next() {
			return (E) data[index++];
		}

	}

	/**
	 * This method is called when <b>any change</b> happens to this {@code Container}.
	 */
	public void refresh() {
		for (Consumer<Container<E>> refresher : this.refreshers)
			refresher.accept(this);
	}

	/**
	 * Removes the specified {@code amount} from an item at the specified {@code index}, if possible,
	 * and returns true if the amount was removed.
	 * 
	 * @param index
	 *            the index of the item to remove the amount from
	 * @param amount
	 *            the amount to be removed from the item
	 * @return true if the amount was removed; return false otherwise
	 */
	public boolean remove(int index, int amount) {
		if (data[index] != null) {
			data[index].setAmount(data[index].getAmount() - amount);
			if (data[index].getAmount() < minimumStack) // TODO < 1 should be <
														// minStack
				data[index] = null;
			refresh();
			return true;
		}
		return false;
	}

	/**
	 * Removes the item at the specified {@code index} of this {@code Container} if possible, and
	 * returns the removed item.
	 * 
	 * @param index
	 *            the index of the item to remove
	 * @return the item removed; return null if non-existent
	 */
	public E remove(int index) {
		if (index == -1)
			throw new IllegalArgumentException("Value less than 0: " + index);
		E e = (E) data[index];
		if (e != null)
			data[index] = null;
		refresh();
		return e;
	}

	/**
	 * Returns the combined amounts of every item within this {@code Container} with the same id as the
	 * specified {@code itemId}.
	 * 
	 * @param itemId
	 *            the id of the items
	 * @return the amount of each item with the same id's combined
	 */
	public int amountOf(int itemId) {
		int amount = 0;
		for (int i = 0; i < capacity; i++)
			if (data[i] != null && data[i].getId() == itemId)
				amount += data[i].getAmount();
		return amount;
	}

	/**
	 * Returns the amount of indices within this {@code Container} that have not been occupied by an
	 * item.
	 * 
	 * @return the amount of free spaces within this {@code Container}
	 */
	public int getFreeSlots() {
		return capacity - size();
	}

	/**
	 * Returns true if this {@code Container} has no free slots open.
	 * 
	 * @return true if no slots open; return false otherwise
	 */
	public boolean isFull() {
		return getFreeSlots() == 0;
	}

	/**
	 * Sorts this {@code Container} to rearrange the items based on the specified {@code Comparator}
	 * argument.
	 * 
	 * @param comparator
	 *            the comparator to sort this {@code Container}.
	 */
	public void sort(Comparator<Item> comparator) {
		this.shift();
		int size = this.size() - this.getFreeSlots();
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (comparator.compare(this.get(i), this.get(j)) < 0) {
					Item tmp = this.get(j);
					this.set(j, this.get(i));
					this.set(i, (E) tmp);
				}
			}
		}
		refresh();
	}

	/**
	 * Shifts the elements within this {@code Container} to the left and leaves no empty spaces in
	 * between each item.
	 */
	public void shift() {
		ArrayList<Item> shifted = new ArrayList<Item>();
		Arrays.asList(data).stream().filter(n -> n != null).forEach(n -> shifted.add(n));
		this.data = shifted.toArray(new Item[capacity]);
		refresh();
	}

	/**
	 * Returns the number of maximum amount of items that is allowed within this {@code Container}.
	 * 
	 * @return the maximum amount of items allowed in this container
	 */
	public int getCapacity() {
		return this.capacity;
	}

	/**
	 * Returns the amount of items within this {@code Container} not equal to null.
	 * 
	 * @return the amount of items
	 */
	public int size() {
		int size = 0;
		for (int i = 0; i < data.length; i++)
			if (data[i] != null)
				size++;
		return size;
	}

	/**
	 * Returns true if this {@code Container} has no items within it.
	 * 
	 * @return true if no items are within this {@code Container}; return false otherwise
	 */
	public boolean isEmpty() {
		return size() == 0;
	}

	/**
	 * Returns true if the specified {@code Object} is contained within this {@code Container} and is
	 * instance of {@code Item}.
	 * 
	 * @param o
	 *            the object to check if container within this {@code Container}
	 * @return true if the object is within this {@code Container}; return false otherwise
	 */
	public boolean contains(Object o) {
		return indexOf(o) != -1;
	}

	/**
	 * Returns true if the specified {@code objects} is contained inside of this {@code Container}.
	 * 
	 * @param objects
	 *            the objects to check
	 * @return true if all objects are contained; return false otherwise
	 */
	public boolean contains(Object... objects) {
		for (Object o : objects)
			if (!contains(o))
				return false;
		return true;
	}

	/**
	 * Swaps the item at each specified index with each other.
	 * 
	 * @param fromIndex
	 *            the index to swap from
	 * @param toIndex
	 *            the index to swap to
	 */
	public void swap(int fromIndex, int toIndex) {
		E old = get(toIndex);
		set(toIndex, get(fromIndex));
		set(fromIndex, old);
		refresh();
	}

	/**
	 * Returns the item element at the specified {@code index} of this {@code Container}.
	 * 
	 * @param index
	 *            the index to get the item at
	 * @return the item at the index; return null if non-existent
	 */
	public E get(int index) {
		return (E) data[index];
	}

	/**
	 * Returns the index of the specified {@code object}, which can be the id of an item or an instance
	 * of an item
	 * 
	 * @param object
	 *            the object to retrieve the index for
	 * @return the index retrieved; return -1 if not found
	 */
	public int indexOf(Object object) {
		if (object instanceof Item) {
			Item item = (Item) object;
			for (int i = 0; i < capacity; i++)
				if (data[i] != null && data[i].getId() == item.getId() && data[i].getAmount() >= item.getAmount())
					return i;
		} else if (object instanceof Integer) {
			for (int i = 0; i < capacity; i++)
				if (data[i] != null && data[i].getId() == (Integer) object)
					return i;
		}
		return -1;
	}

	/**
	 * Inserts the specified {@code element} at the {@code index} of this {@code Container}.
	 * 
	 * @param index
	 *            the index to insert the item at
	 * @param element
	 *            the item to be inserted
	 * @return true if the item was inserted; return false otherwise
	 */
	public boolean insert(int index, E element) {
		if (getFreeSlots() == 0)
			return false;
		E[] items = (E[]) Arrays.copyOf(data, data.length);
		for (int i = index + 1; i < capacity; i++)
			set(i, items[i - 1]);
		data[index] = element;
		refresh();
		return true;
	}

	/**
	 * Sets the specified {@code element} at the {@code index} of this {@code Container} and returns the
	 * item which was previously there.
	 * 
	 * @param index
	 *            the index to set the item at
	 * @param element
	 *            the item to set at the index
	 * @return the previous item which was placed at the index; return null if there wasn't one
	 */
	public E set(int index, E element) {
		E e = (E) (data[index] = element);
		refresh();
		return e;
	}

	/**
	 * Returns the {@code Iterator} of this {@code Container}.
	 * 
	 * @return the iterator
	 */
	public ContainerIterator iterator() {
		return new ContainerIterator();
	}

	/**
	 * Converts the items within this {@code Container} to an array, which contains nulled objects if
	 * there is no item within that place in the array.
	 */
	public E[] toArray() {
		return (E[]) Arrays.copyOf(data, data.length);
	}

	/*
	 * {@inheritDoc}
	 */
	public <T> T[] toArray(T[] a) {
		return (T[]) Arrays.copyOf(data, size(), a.getClass());
	}

	/*
	 * {@inheritDoc}
	 */
	public String toString() {
		return Arrays.toString(this.data);
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean add(Item e) {
		if (e == null)
			return false;
		boolean added = handler.add(this, (E) e);
		refresh();
		return added;
	}

	/**
	 * Adds a new {@code Item} to this {@code Container} with the specified {@code id} and
	 * {@code amount}.
	 */
	public boolean add(int id, int amount) {
		return this.add(new Item(id, amount));
	}

	/**
	 * Adds 1 new {@code Item} to this {@code Container} with the specified {@code id}.
	 */
	public boolean add(int id) {
		return this.add(new Item(id, 1));
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean remove(Object o) {
		if (o == null || !(o instanceof Item))
			return false;
		boolean removed = handler.remove(this, (E) o);
		refresh();
		return removed;
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean addAll(Collection<? extends E> c) {
		for (Item item : c)
			if (item != null && !add(item))
				return false;
		return true;
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean removeAll(Collection<?> c) {
		for (Object item : c)
			if (!remove(item))
				return false;
		return true;
	}

	/*
	 * {@inheritDoc}
	 */
	public boolean retainAll(Collection<?> c) {
		c.stream().filter(n -> !contains(n)).forEach(n -> remove(n));
		return this.containsAll(c);
	}

	/**
	 * Clears this {@code Container} of every item and replaces the value of the items with null.
	 */
	public void clear() {
		for (int i = 0; i < capacity; i++)
			data[i] = null;
		refresh();
	}

	/**
	 * Returns an index which has not been taken inside of this {@code Container}.
	 * 
	 * @return a free index; return -1 if none
	 */
	public int getFreeIndex() {
		for (int i = 0; i < capacity; i++)
			if (data[i] == null)
				return i;
		return -1;
	}

	/**
	 * Returns the maximum item stack amount an item in this {@code Container} can have.
	 * 
	 * @return the maximum stack amount
	 */
	public int getMaximumStack() {
		return maximumStack;
	}

	/**
	 * Returns the minimum item stack amount an item in this {@code Container} can have.
	 * 
	 * @return the minimum stack amount
	 */
	public int getMinimumStack() {
		return minimumStack;
	}

	/**
	 * 
	 * @param refresher
	 */
	public void addRefreshListener(Consumer<Container<E>> refresher) {
		this.refreshers.add(refresher);
	}

	public void removeRefreshListener(Consumer<Container<E>> refresher) {
		this.refreshers.remove(refresher);
	}

	@Override
	public ConfigSection serialize() {
		ConfigSection config = new ConfigSection();
		for (int i = 0; i < this.capacity; i++) {
			Item item = this.get(i);
			if (item != null)
				config.set("" + i, new ConfigSection().set("id", item.getId()).set("amount", item.getAmount()));
		}
		return config;
	}

	@Override
	public void deserialize(ConfigSection section) {
		for (Entry<Object, Object> entry : section.entrySet()) {
			ConfigSection map = new ConfigSection((Map<Object, Object>) entry.getValue());

			int slot = Integer.parseInt("" + entry.getKey());
			int id = map.getInt("id");
			int amount = map.getInt("amount");
			this.set(slot, (E) new Item(id, amount));
		}
	}

}
