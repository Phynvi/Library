package container;

/**
 * A class that holds different methods for a {@code Container}.
 * 
 * @author Albert Beaupre
 * 
 * @see container.Container
 * @see container.BasicItem
 */
public class Containers {

    /**
     * This {@code ContainerType} will make sure that every
     * {@code BasicItemStack} added to a {@code Container} will be stacked by
     * any possible means.
     */
    public static ContainerHandler<BasicItem> ALWAYS_STACK = new ContainerHandler<BasicItem>() {

	@Override
	public boolean add(Container<BasicItem> container, BasicItem item) {
	    int id = item.getId();
	    int amount = item.getAmount();
	    int index = container.indexOf(item.getId());
	    int maxStack = container.getMaximumStack();
	    if (index != -1) {
		int there = container.get(index).getAmount();
		int total = there + amount > maxStack ? maxStack : there + amount;
		container.set(index, new BasicItem(id, total));
	    } else {
		if (container.isFull())
		    return false;
		container.set(container.getFreeIndex(), new BasicItem(id, amount > maxStack ? maxStack : amount));
	    }
	    return true;
	}

	@Override
	public boolean remove(Container<BasicItem> container, BasicItem item) {
	    int index = container.indexOf(item.getId());
	    if (index != -1) {
		BasicItem previous = container.get(index);
		previous.amount(previous.getAmount() - item.getAmount());
		if (previous.getAmount() < container.getMinimumStack())
		    container.set(index, null);
		return true;
	    }
	    return false;
	}

	@Override
	public boolean addable(Container<BasicItem> container, BasicItem item) {
	    return false;
	}
    };

    /**
     * This {@code ContainerType} will make sure that every
     * {@code BasicItemStack} is placed into a {@code Container} by a regular
     * stacking order.
     */
    public static ContainerHandler<BasicItem> AVAILABLE_STACK = new ContainerHandler<BasicItem>() {

	@Override
	public boolean add(Container<BasicItem> container, BasicItem item) {
	    int id = item.getId();
	    int amount = item.getAmount();
	    if (item.isStackable()) {
		int index = container.indexOf(item.getId());
		int maxStack = container.getMaximumStack();
		if (index != -1) {
		    int there = container.get(index).getAmount();
		    int total = there + amount > maxStack ? maxStack : there + amount;
		    container.set(index, new BasicItem(id, total));
		} else {
		    if (container.isFull())
			return false;
		    container.set(container.getFreeIndex(), new BasicItem(id, amount > maxStack ? maxStack : amount));
		}
		return true;
	    } else {
		if (container.isFull())
		    return false;
		for (int i = 0; i < amount; i++) {
		    int freeIndex = container.getFreeIndex();
		    if (freeIndex != -1) {
			container.set(freeIndex, new BasicItem(id, 1));
		    } else {
			return false;
		    }
		}
		return true;
	    }
	}

	@Override
	public boolean remove(Container<BasicItem> container, BasicItem item) {
	    if (item.isStackable()) {
		int index = container.indexOf(item.getId());
		if (index != -1) {
		    BasicItem previous = container.get(index);
		    previous.amount(previous.getAmount() - item.getAmount());
		    if (previous.getAmount() < container.getMinimumStack())
			container.set(index, null);
		    return true;
		}
	    } else {
		for (int i = 0; i < item.getAmount(); i++) {
		    int index_2 = container.indexOf(item.getId());
		    if (index_2 != -1) {
			container.set(index_2, null);
		    } else {
			return false;
		    }
		}
	    }
	    return true;
	}

	@Override
	public boolean addable(Container<BasicItem> container, BasicItem item) {
	    return false;
	}
    };

    /**
     * This {@code ContainerType} makes sure that every {@code BasicItemStack}
     * placed into a {@code Container} will never stack.
     */
    public static ContainerHandler<BasicItem> NEVER_STACK = new ContainerHandler<BasicItem>() {

	@Override
	public boolean add(Container<BasicItem> container, BasicItem item) {
	    if (container.isFull())
		return false;
	    for (int i = 0; i < item.getAmount(); i++) {
		int freeIndex = container.getFreeIndex();
		if (freeIndex != -1) {
		    container.set(freeIndex, new BasicItem(item.getId(), 1));
		} else {
		    return false;
		}
	    }
	    return true;
	}

	@Override
	public boolean remove(Container<BasicItem> container, BasicItem item) {
	    for (int i = 0; i < item.getAmount(); i++) {
		int index_2 = container.indexOf(item.getId());
		if (index_2 != -1) {
		    container.set(index_2, null);
		} else {
		    return false;
		}
	    }
	    return true;
	}

	@Override
	public boolean addable(Container<BasicItem> container, BasicItem item) {
	    return false;
	}
    };

    /**
     * Creates a {@code Container} with a minimum stack value of 0 and a maximum
     * stack value of {@link Integer#MAX_VALUE}.
     * 
     * @param handler
     *            the handler to set for the container
     * @param capacity
     *            the capacity of the container
     * @return the created container
     */
    public static <E extends BasicItem> Container<E> create(ContainerHandler<E> handler, int capacity) {
	return new Container<E>(handler, capacity, 0, Integer.MAX_VALUE);
    }

}
