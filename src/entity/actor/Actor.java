package entity.actor;

/**
 * Represents a {@code ActionQueue} holder that handles any {@code Action} queuing. The
 * {@code Actor} is used within any {@code Action} as an 'owner' to be running the {@code Action}.
 * 
 * @author Albert Beaupre
 * 
 * @see entity.actor.ActionQueue
 */
public interface Actor {

	/**
	 * Returns the {@code ActionQueue} of this {@code Actor}.
	 * 
	 * @return the action queue
	 */
	@SuppressWarnings("rawtypes")
	ActionQueue getActions();

	/**
	 * Queues the specified {@code action} to the {@code ActionQueue} that this {@code Actor} contains.
	 * 
	 * <p>
	 * This is effectively equivalent to:
	 * 
	 * <pre>
	 * getActions().queue(action);
	 * </pre>
	 * 
	 * @param action
	 *            the action to queue
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public default void queue(Action action) {
		getActions().queue(action);
	}

}
