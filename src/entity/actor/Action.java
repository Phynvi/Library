package entity.actor;

/**
 * @author Albert Beaupre
 *
 * @param <A>
 *            The {@code Actor} type of this {@code Action}.
 * 
 * @see entity.actor.Actor
 */
public abstract class Action<A extends Actor> {

    /**
     * The {@code Actor} type of which this {@code Action} is being run by.
     */
    protected A actor;

    private ActionState state; // The current state which this Action is at

    /**
     * Constructs a new {@code Action} from the specified {@code actor} as the
     * owner of this {@code Action}.
     * 
     * @param actor
     *            the owner of this entity.actor.action
     */
    public Action(A actor) {
	this.actor = actor;

	this.state = ActionState.START;
    }

    /**
     * Cycles through this {@code Action} until {@code false} is returned or
     * until the specified {@code state} is equal to {@link ActionState#STOP}.
     * 
     * @param state
     *            the state at which this {@code Action} is at
     * @return true if the {@code Action} continues to cycle; return false
     *         otherwise
     */
    public abstract boolean cycle(ActionState state);

    /**
     * If this method is set to false, then this {@code Action} <b>must</b>
     * finish before it ends.
     * 
     * @return true if cancellable; return false otherwise
     */
    public abstract boolean cancellable();

    /**
     * Continues onto the next cycle of this {@code Action} and returns true if
     * the state isn't already at {@link ActionState#STOP}; otherwise it returns
     * true and sets the state to the next state.
     * 
     * @return true if the next cycle can be made; return false otherwise
     */
    public boolean nextCycle() {
	if (state == ActionState.FINISH)
	    return false;
	state = ActionState.values()[state.ordinal() + 1];
	return true;
    }

    /**
     * Sets the state of this {@code Action} to the specified {@code state}
     * argument.
     * 
     * @param state
     *            the state to set to this {@code Action}
     */
    public void setState(ActionState state) {
	this.state = state;
    }

    /**
     * Returns the current {@code ActionState} of this {@code Action}.
     * 
     * @return the current state
     */
    public ActionState getState() {
	return this.state;
    }

}
