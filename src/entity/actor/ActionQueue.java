package entity.actor;

import infrastructure.Tick;

/**
 * Represents a type of 'Queue' for queuing {@code Action} types so they may be
 * cycled. {@code ActionQueue} extends the {@link infrastructure.Tick} class so
 * it may cycle through actions continuously until all actions are gone and it
 * will stop itself from cycling anymore until another {@code Action} has been
 * queued.
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

	private Action<A> currentAction; // The queue of actions that will be cycled
	private boolean hasUncancellable;

	/**
	 * Constructs a new {@code ActionQueue}
	 */
	public ActionQueue() {}

	/**
	 * Queues the specified {@code Action} to this {@code ActionQueue} so it
	 * will eventually be cycled.
	 * 
	 * @param entity
	 *            .actor.action the entity.actor.action to queue.
	 */
	public void queue(Action<A> action) {
		if (currentAction != null) {
			if (currentAction.cancellable()) {
				if (currentAction.getState().equals(ActionState.START) || currentAction.getState().equals(ActionState.RUNNING))
					cancel(currentAction);
				this.currentAction = action;
			} else {
				this.hasUncancellable = true;
				return;
			}
		} else {
			this.currentAction = action;
		}
		if (!this.isQueued())
			this.queue();
	}

	/**
	 * Clears any {@code Action} queued in this {@code ActionQueue} that has the
	 * same priority or lower as the specified value.
	 * 
	 * @param priority
	 *            the priorities to clear that are less than or equal to this
	 *            value
	 */
	public void clear() {
		this.cancel(currentAction);
	}

	public void clearForcefully() {
		if (currentAction == null)
			return;
		currentAction.setState(ActionState.CANCEL);
		currentAction.cycle(ActionState.CANCEL);
		this.currentAction = null;
	}

	/**
	 * Cancels the specified {@code entity.actor.action} if within this
	 * {@code ActionQueue} and sets its {@code ActionState} to
	 * {@link ActionState#CANCEL} and cycles it through the cancel state before
	 * it completely removes it from the queue.
	 * 
	 * @param entity
	 *            .actor.action the entity.actor.action to be cancelled
	 */
	public void cancel(Action<A> action) {
		if (action == null)
			return;
		if (action.cancellable()) {
			action.setState(ActionState.CANCEL);
			action.cycle(ActionState.CANCEL);
		}
	}

	/**
	 * This method only runs when there are actions queued.
	 * 
	 * <p>
	 * This method executes any actions in its queue in the order that each
	 * entity.actor.action was queued in. As an {@code Action} that was executed
	 * stops running, it is removed from the current queue and the next
	 * entity.actor.action is executed. If an {@link Action#cancellable()}
	 * returns false, then the next entity.actor.action in the queue <b>will
	 * not</b> execute until the current entity.actor.action is completely
	 * stopped.
	 */
	public final void tick() {
		if (this.currentAction == null) {
			this.cancel();
			return;
		}

		if (!currentAction.cancellable())
			this.hasUncancellable = true;
		Action<A> c = this.currentAction;
		if (!currentAction.cycle(currentAction.getState())) {
			if (c == this.currentAction) {
				this.currentAction = null;
				queue();
				return;
			}
		}
		this.queue(600);
	}

	/**
	 * @return the hasUncancellable
	 */
	public boolean hasUncancellable() {
		return hasUncancellable;
	}
}