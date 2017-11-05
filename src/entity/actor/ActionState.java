package entity.actor;

/**
 * Represents a State at which an {@code Action} is at in cycling
 * 
 * @author Albert Beaupre
 *
 * @see {@link Action#cycle(ActionState)}
 * @see {@link Action#setState(ActionState)}
 * @see {@link Action#getState()}
 */
public enum ActionState {

	/**
	 * The starting cycle of an {@code Action}, which is the beginning state of the {@code Action}.
	 */
	START,

	/**
	 * The state of an {@code Action} where it is just "running through" or "cycling through" the
	 * {@code Action}.
	 */
	RUNNING,

	/**
	 * The finishing state of an {@code Action} that is called when the {@code Action} is "finishing
	 * up" or finalizing things.
	 */
	FINISH,

	/**
	 * The state of an {@code Action} where it has been cancelled by an {@code ActionQueue}.
	 */
	CANCEL;

}
