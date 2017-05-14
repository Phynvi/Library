package action;

/**
 * Represents a {@code ActionQueue} holder that handles any {@code Action}
 * queueing.
 * 
 * @author Albert Beaupre
 * 
 * @see action.ActionQueue
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
     * Queues the specified {@code action} to the {@code ActionQueue} that this
     * {@code Actor} contains.
     * 
     * @param action
     *            the action to queue
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public default void queue(Action action) {
	getActions().queue(action);
    }

}
