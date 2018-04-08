package infrastructure;

/**
 * Represents a type of 'Tick' that will execute based on its {@link Tick#getPeriod()} value.
 * 
 * <p>
 * <b>Example: This will print "Hello World" in 10 seconds</b>
 * 
 * <pre>
 * new Tick() {
 *     &#064;Override
 *     protected void tick() {
 * 	System.out.println(&quot;Hello World&quot;);
 *     }
 * }.queue(<u><b>10000</b></u>);
 * </pre>
 * 
 * @author Albert Beaupre
 */
public abstract class Tick {

	private boolean queued; // The flag that checks if this Tick has been queued for execution
	private boolean cancelled; // The flag that checks if this Tick has been cancelled so it cannot be executed anymore
	private long startedTicking; // The time when this Tick has started tracking its duration
	private long period; // The time period of delay before it will be executed

	/**
	 * Executes this {@code Tickable} after the {@link #queue(long)} method has been called and the
	 * {@link getDuration()} value is greater or equal to {@link Tick#getPeriod()}.
	 */
	public abstract void tick();

	/**
	 * Starts a period of time at which this {@code Tick} has started updating. This method should be
	 * used by a {@code TickThread} only.
	 */
	public void startTicking() {
		this.startedTicking = System.currentTimeMillis();
	}

	/**
	 * Returns the period at which this {@code Tick} delays before its next update.
	 * 
	 * @return the delay before its next update
	 */
	public long getPeriod() {
		return period;
	}

	/**
	 * Returns the length in milliseconds that this {@code Tick} has been waiting to be executed.
	 * 
	 * @return the duration
	 */
	public long getDuration() {
		return System.currentTimeMillis() - startedTicking;
	}

	/**
	 * Queues this {@code Tickable} for execution after the specified {@code period} has passed.
	 * 
	 * @param period
	 *            the period before this tickable will queued for execution.
	 */
	public void queue(long period) {
		this.period = period;
		if (queued)
			return;
		GlobalVariables.getTicker().queue(this);
		queued = true;
		cancelled = false;
	}

	/**
	 * Queues this {@code Tickable} instantly. This method is effectively equivalent to calling:
	 * 
	 * <pre>
	 * queue(ticker, 0);
	 * </pre>
	 */
	public void queue() {
		queue(0);
	}

	/**
	 * Returns true if this {@code Tick} is currently queued.
	 * 
	 * @return true if queued; return false otherwise
	 */
	public boolean isQueued() {
		return queued;
	}

	/**
	 * Cancels this {@code Tick} from being queued or executed.
	 */
	public void cancel() {
		cancelled = true;
		queued = false;
	}

	/**
	 * Returns true if this {@code Tick} has been cancelled;
	 * 
	 * @return true if cancelled; return false otherwise
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getClass().getName() + ": [period=" + period + ", cancelled=" + cancelled + ", queued=" + queued + "]";
	}
}