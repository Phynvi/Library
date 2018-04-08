package container;

/**
 * Represents a handler for a {@code Container} when making any changes with an {@code Item}.
 * 
 * @author Albert Beaupre
 *
 * @param <E>
 *            the item element type
 * 
 * @see container.Item
 * @see container.Container
 */
public interface ContainerHandler<E extends Item> {

	/**
	 * Adds the specified {@code item} to the {@code container}, if possible, and returns true if the
	 * {@code item} was added.
	 * 
	 * @param container
	 *            the container to add the item to
	 * @param item
	 *            the item to add
	 * @return true if the item was added; return false otherwise
	 */
	boolean add(Container<E> container, Item item);

	/**
	 * Removes the specified {@code item} from the {@code container}, if possible, and returns true if
	 * the {@code item} was removed.
	 * 
	 * @param container
	 *            the container to remove the item from
	 * @param item
	 *            the item to remove
	 * @return true if the item was removed; return false otherwise
	 */
	boolean remove(Container<E> container, Item item);

	/**
	 * Returns true if the specified {@code item} can be added to the {@code container}.
	 * 
	 * @param container
	 *            the container to check
	 * @param item
	 *            the item to use for checking
	 * @return true if the item can be added; return false otherwise
	 */
	boolean addable(Container<E> container, Item item);

}
