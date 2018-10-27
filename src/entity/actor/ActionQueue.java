package entity.actor;

import infrastructure.Tick;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Represents a type of 'Queue' for queuing {@code Action} types so they may be cycled.
 * {@code ActionQueue} extends the {@link infrastructure.Tick} class so it may cycle through actions
 * continuously until all actions are gone and it will stop itself from cycling anymore until
 * another {@code Action} has been queued.
 * 
 * @author Albert Beaupre
 * 
 * @param <A>
 *            The {@code Actor} type that will be queuing its actions
 * 
 * @see entity.actor.Action
 * @see entity.actor.Actor
 */
public final class ActionQueue<A extends Actor> extends Tick {

	private final LinkedList<Action<A>> actions; // The queue of actions that will be cycled

	/**
	 * Constructs a new {@code ActionQueue}
	 */
	public ActionQueue() {
		this.actions = new LinkedList<>();
	}

	/**
	 * Queues the specified {@code Action} to this {@code ActionQueue} so it will eventually be cycled.
	 * 
	 * @param entity
	 *            .actor.action the entity.actor.action to queue.
	 */
	public void queue(Action<A> action) {
		Iterator<Action<A>> iterator = this.actions.iterator();
		while (iterator.hasNext()) {
			Action<A> a = iterator.next();
			if (a.cancellable() && a.getState() != ActionState.FINISH)
				this.cancel(a);
		}
		this.actions.offer(action);
		this.queue();
	}

	/**
	 * Clears any {@code Action} queued in this {@code ActionQueue} that has the same priority or lower
	 * as the specified value.
	 * 
	 * @param priority
	 *            the priorities to clear that are less than or equal to this value
	 */
	public void clear() {
		Iterator<Action<A>> iterator = actions.iterator();
		while (iterator.hasNext()) {
			Action<A> action = iterator.next();
			if (action.cancellable())
				this.cancel(action);
		}
	}

	public void clearForcefully() {
		Iterator<Action<A>> iterator = actions.iterator();
		while (iterator.hasNext()) {
			Action<A> action = iterator.next();
			this.cancel(action);
		}
	}

	/**
	 * Cancels the specified {@code entity.actor.action} if within this {@code ActionQueue} and sets its
	 * {@code ActionState} to {@link ActionState#CANCEL} and cycles it through the cancel state before
	 * it completely removes it from the queue.
	 * 
	 * @param entity
	 *            .actor.action the entity.actor.action to be cancelled
	 */
	public void cancel(Action<A> action) {
		if (action.cancellable() && this.actions.remove(action)) {
			action.setState(ActionState.CANCEL);
			action.cycle(ActionState.CANCEL);
		}
	}

	/**
	 * This method only runs when there are actions queued.
	 * 
	 * <p>
	 * This method executes any actions in its queue in the order that each entity.actor.action was
	 * queued in. As an {@code Action} that was executed stops running, it is removed from the current
	 * queue and the next entity.actor.action is executed. If an {@link Action#cancellable()} returns
	 * false, then the next entity.actor.action in the queue <b>will not</b> execute until the current
	 * entity.actor.action is completely stopped.
	 */
	public final void tick() {
		if (this.actions.isEmpty())
			return;
		Action<A> action = this.actions.peek();
		if (action != null) {
			if (!action.cycle(action.getState())) {
				this.actions.remove();
				this.queue(600);
			} else {
				this.queue(600);
			}
			return;
		}
	}
}
