package infrastructure.timing;

import infrastructure.Attachments;
import infrastructure.Core;
import infrastructure.CoreThread;

import java.util.concurrent.CopyOnWriteArrayList;
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
 * @see infrastructure.timing.Tick
 */
public class Ticker extends CoreThread {

    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    private CopyOnWriteArrayList<Tick> list; // This list is filled by any incoming ticks to be executed
    private boolean running;

    /**
     * Constructs a new {@code Ticker} with an empty list of {@code Ticks}.
     */
    public Ticker() {
	super("Ticker Thread" + Math.random(), Thread.NORM_PRIORITY, false);
	this.list = new CopyOnWriteArrayList<>();
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
	this.list.add(tickable);
	if (!running)
	    Core.submit(Ticker.this);
	running = true;
    }

    /**
     * This method should be run on a constant loop to continuously check,
     * execute, and remove any {@code Tick} within this {@code Ticker}.
     */
    public void run() {
	try {
	    while (list.size() > 0) {
		running = true;
		for (int i = 0; i < list.size(); i++) {
		    Tick tickable = list.get(i);
		    if (tickable == null)
			continue;
		    if (tickable.isCancelled()) {
			list.remove(i);
			continue;
		    }
		    if (tickable.getDuration() >= tickable.getPeriod()) {
			tickable.startTicking();
			tickable.tick();
		    }
		}
	    }
	    running = false;
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }
}
