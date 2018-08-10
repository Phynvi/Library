package container;

/**
 * A class that holds different methods for a {@code Container}.
 * 
 * @author Albert Beaupre
 * 
 * @see container.Container
 * @see container.Item
 */
public class Containers {

	/**
	 * This {@code ContainerType} will make sure that every {@code BasicItemStack} added to a
	 * {@code Container} will be stacked by any possible means.
	 */
	public static ContainerHandler<Item> ALWAYS_STACK = new ContainerHandler<Item>() {

		@Override
		public boolean add(Container<Item> c, Item item) {
			int id = item.getId();
			int amount = item.getAmount();
			int index = c.indexOf(item.getId());
			int maxStack = item.getMaximumStackValue() != c.getMaximumStack() ? item.getMaximumStackValue() : c.getMaximumStack();
			if (index != -1) {
				int there = c.get(index).getAmount();
				int total = there + amount > maxStack ? maxStack : there + amount;
				c.set(index, new Item(id, total));
			} else {
				if (c.isFull())
					return false;
				c.set(c.getFreeIndex(), new Item(id, amount > maxStack ? maxStack : amount));
			}
			return true;
		}

		@Override
		public boolean remove(Container<Item> c, Item item) {
			int index = c.indexOf(item.getId());
			if (index != -1) {
				Item previous = c.get(index).decrease(item.getAmount());
				if (previous.getAmount() < c.getMinimumStack())
					c.set(index, null);
				return true;
			}
			return false;
		}

		@Override
		public boolean addable(Container<Item> container, Item item) {
			return false;
		}
	};

	/**
	 * This {@code ContainerType} will make sure that every {@code BasicItemStack} is placed into a
	 * {@code Container} by a regular stacking order.
	 */
	public static ContainerHandler<Item> AVAILABLE_STACK = new ContainerHandler<Item>() {

		@Override
		public boolean add(Container<Item> c, Item item) {
			int id = item.getId();
			int amount = item.getAmount();
			if (item.isStackable()) {
				int index = c.indexOf(item.getId());
				int maxStack = item.getMaximumStackValue() != c.getMaximumStack() ? item.getMaximumStackValue() : c.getMaximumStack();
				if (index != -1) {
					int there = c.get(index).getAmount();
					int total = there + amount > maxStack ? maxStack : there + amount;
					c.set(index, new Item(id, total));
				} else {
					if (c.isFull())
						return false;
					c.set(c.getFreeIndex(), new Item(id, amount > maxStack ? maxStack : amount));
				}
				return true;
			} else {
				if (c.isFull())
					return false;
				for (int i = 0; i < amount; i++) {
					int freeIndex = c.getFreeIndex();
					if (freeIndex != -1) {
						c.set(freeIndex, new Item(id, 1));
					} else {
						return false;
					}
				}
				return true;
			}
		}

		@Override
		public boolean remove(Container<Item> c, Item item) {
			if (item.isStackable()) {
				int index = c.indexOf(item.getId());
				if (index != -1) {
					Item previous = c.get(index).decrease(item.getAmount());
					if (previous.getAmount() < c.getMinimumStack())
						c.set(index, null);
					return true;
				}
			} else {
				for (int i = 0; i < item.getAmount(); i++) {
					int index_2 = c.indexOf(item.getId());
					if (index_2 != -1) {
						c.set(index_2, null);
					} else {
						return false;
					}
				}
			}
			return true;
		}

		@Override
		public boolean addable(Container<Item> c, Item item) {
			return false;
		}
	};

	/**
	 * This {@code ContainerType} makes sure that every {@code BasicItemStack} placed into a
	 * {@code Container} will never stack.
	 */
	public static ContainerHandler<Item> NEVER_STACK = new ContainerHandler<Item>() {

		@Override
		public boolean add(Container<Item> c, Item item) {
			if (c.isFull())
				return false;
			for (int i = 0; i < item.getAmount(); i++) {
				int freeIndex = c.getFreeIndex();
				if (freeIndex != -1) {
					c.set(freeIndex, new Item(item.getId(), 1));
				} else {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean remove(Container<Item> c, Item item) {
			for (int i = 0; i < item.getAmount(); i++) {
				int index_2 = c.indexOf(item.getId());
				if (index_2 != -1) {
					c.set(index_2, null);
				} else {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean addable(Container<Item> c, Item item) {
			return false;
		}
	};

	/**
	 * Creates a {@code Container} with a minimum stack value of 0 and a maximum stack value of
	 * {@link Integer#MAX_VALUE}.
	 * 
	 * @param handler
	 *            the handler to set for the container
	 * @param capacity
	 *            the capacity of the container
	 * @return the created container
	 */
	public static <E extends Item> Container<E> create(ContainerHandler<E> handler, int capacity) {
		return new Container<E>(handler, capacity, 0, Integer.MAX_VALUE);
	}

}
