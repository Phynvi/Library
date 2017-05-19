package timing;

import infrastructure.Attachments;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

/**
 * The {@code Ticker} class is used to handle any {@code Tick}. If a
 * {@code Tick} has been queued, it will be executed as soon as
 * {@link Tick#getDuration()} is greater or equal to {@link Tick#getPeriod()}
 * and then it will be stopped and removed from this {@code Ticker} until
 * {@link Tick#queue(long)} has been called again.
 * 
 * <p>
 * A {@code Ticker} should be constantly running in a {@code Thread} to
 * continuously handle Ticks:
 * 
 * <pre>
 * {
 *     Ticker ticker = new Ticker();
 * 
 *     ScheduledExecutorService SERVICE = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());
 *     SERVICE.scheduleAtFixedRate(ticker, 1, 1, TimeUnit.MILLISECONDS); //This will schedule the ticker continuously at 1 millisecond delay
 * }
 * </pre>
 * 
 * @author Albert Beaupre
 * 
 * @see timing.Tick
 */
public class Ticker implements Runnable {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private ArrayList<Tick> list; // This list is filled by any incoming ticks to be executed
    private ArrayList<Tick> modifier; // This list holds and Tick that is going to be added to the list of executable ticks

    /**
     * Constructs a new {@code Ticker} with an empty list of {@code Ticks}.
     */
    public Ticker() {
	this.list = new ArrayList<>();
	this.modifier = new ArrayList<>();
    }

    /**
     * Queues the specified {@code Tick} to be executed when
     * {@link Tick#getDuration()} is greater or equal to
     * {@link Tick#getPeriod()} and them it will be removed from this
     * {@code Ticker} until this method is called again.
     * 
     * @param tickable
     *            the {@code Tick} to be queued to execute
     */
    public void queue(Tick tickable) {
	if (Attachments.getTicker() == null) {
	    LOGGER.severe("There is not a ticker attached to queue this Tick: " + tickable);
	    return;
	}
	tickable.startTicking();
	this.modifier.add(tickable);
    }

    /**
     * This method should be run on a constant loop to continuously check,
     * execute, and remove any {@code Tick} within this {@code Ticker}.
     */
    public void run() {
	while (true) {
	    if (modifier.size() > 0) {
		list.addAll(modifier);
		modifier = new ArrayList<>();
	    }
	    Iterator<Tick> iterator = list.iterator();
	    while (iterator.hasNext()) {
		Tick tickable = iterator.next();
		if (tickable == null)
		    continue;
		if (tickable.isCancelled()) {
		    iterator.remove();
		    continue;
		}
		if (tickable.getDuration() >= tickable.getPeriod()) {
		    tickable.startTicking();
		    tickable.tick();
		}
	    }
	}
    }
}
