package event;

/**
 * The {@code Event} class represents an actual event occurring. Events are used
 * to help organize actions in a program relevant to themselves. For example, if
 * you wish to create an {@code Event} relevant only to a specific entity called
 * {@code Animal}, then you would create an {@code Event} called
 * {@code AnimalEvent} and call that event whenever a relevant
 * {@code AnimalEvent} action would occur.
 * 
 * <p>
 * The {@link event.EventManager} class is used to manage the calling,
 * registering, and unregistering of an {@code Event}.
 * 
 * @see event.EventManager
 * @see event.EventListener
 * @see event.EventMethod
 * 
 * @author Albert Beaupre
 * 
 * @version 1.0
 */
public class Event {

    private boolean consumed; // This will flag whether or not this specific event has been used.
    private boolean cancelled; // This will flag whether or not this specific event has been cancelled.

    /**
     * This will switch the {@code consumed} flag of this {@code Event} to be on
     * so the {@link event.EventManager} will know it has been consumed and will
     * <b>not continue to be called.</b>
     */
    public void consume() {
	this.consumed = true;
    }

    /**
     * Returns {@code true} if this {@code Event} has been flagged as consumed
     * so it will <b>not continue to be called</b> by the
     * {@link event.EventManager}, otherwise returns {@code false}.
     * 
     * @return true if flagged as consumed; return false otherwise
     */
    public boolean isConsumed() {
	return this.consumed;
    }

    /**
     * This will switch the {@code cancelled} flag of this {@code Event} to be
     * on so the {@link event.EventManager} will know it has been cancelled and
     * will <b>not be called at all.</b>
     */
    public void cancel() {
	this.cancelled = true;
    }

    /**
     * Returns {@code true} if this {@code Event} has been flagged as cancelled
     * so it will <b>not be called at all</b> by the {@link event.EventManager},
     * otherwise returns {@code false}.
     * 
     * @return true if flagged as cancelled; return false otherwise
     */
    public boolean isCancelled() {
	return cancelled;
    }

}
