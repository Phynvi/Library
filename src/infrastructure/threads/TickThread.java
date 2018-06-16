package infrastructure.threads;

import java.util.ArrayList;

import infrastructure.CoreThread;
import infrastructure.Tick;

/**
 * The {@code TickThread} class is used to handle any {@code Tick}. If a {@code Tick} has been
 * queued, it will be executed as soon as {@link Tick#getDuration()} is greater or equal to
 * {@link Tick#getPeriod()} and then it will be stopped and removed from this {@code TickThread}
 * until {@link Tick#queue(long)} has been called again.
 * 
 * <p>
 * If you wish to use a TickThread separated from the {@link infrastructure.GlobalVariables} class,
 * then you must do something like this:
 * 
 * <pre>
 * {
 * 	TickThread ticker = new TickThread();
 * 
 * 	ExecutorService service = Executors.newCachedThreadPool();
 * 	service.submit(ticker);
 * }
 * </pre>
 * 
 * @author Albert Beaupre
 * 
 * @see infrastructure.Tick
 */
public final class TickThread extends CoreThread {

	private ArrayList<Tick> waitingList = new ArrayList<>();
	private ArrayList<Tick> list; // This list is filled by any
									// incoming ticks to be executed

	/**
	 * Constructs a new {@code TickThread} with an empty list of {@code Ticks}.
	 */
	public TickThread() {
		super("Tick Thread", Thread.MAX_PRIORITY, false);
		this.list = new ArrayList<>();
	}

	/**
	 * Queues the specified {@code Tick} to be executed when {@link Tick#getDuration()} is greater or
	 * equal to {@link Tick#getPeriod()} and then it will be removed from this {@code TickThread} until
	 * this method is called again.
	 * 
	 * @param tickable
	 *            the {@code Tick} to be queued to execute
	 */
	public void queue(Tick tickable) {
		waitingList.add(tickable);
		tickable.startTicking();
	}

	/**
	 * This method should be run on a constant loop to continuously check, execute, and remove any
	 * {@code Tick} within this {@code TickThread}.
	 */
	public void run() {
		try {
			if (waitingList.size() > 0) {
				list.addAll(waitingList);
				waitingList.clear();
			}
			if (list.size() > 0) {

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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
