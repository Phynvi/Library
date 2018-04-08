package entity.actor;

import java.util.function.Consumer;
import entity.actor.model.Model;

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

	/**
	 * Queues a newly created {@code Action} that executes code based on the code within the given
	 * {@code consumer} argument. This allows for quick queuing of an {@code Action}.
	 * 
	 * @param cancellable
	 *            true if the action is cancellable; false otherwise
	 * @param consumer
	 *            the consumer action to execute for the action
	 */
	public default void queue(boolean cancellable, Consumer<ActionState> consumer) {
		this.queue(new Action<Actor>(this) {
			@Override
			public boolean cycle(ActionState state) {
				consumer.accept(state);
				return false;
			}

			@Override
			public boolean cancellable() {
				return cancellable;
			}
		});
	}

	/**
	 * Returns the {@code Model} of this {@code Actor}.
	 * 
	 * @return the model
	 */
	public Model getModel();

}
