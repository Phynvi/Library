package infrastructure;

import infrastructure.threads.ActorUpdateThread;
import infrastructure.threads.TickThread;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import event.EventManager;

/**
 * TheAJ has very good information on threads for a RuneScape Private ServerThread
 * 
 * https://www.rune-server.ee/runescape-development/rs-503-client-server/informative-threads/171504-multithreading.html
 */

/**
 * @author Albert Beaupre
 */
public class Core {

    private static final ScheduledExecutorService threadPool = Executors.newScheduledThreadPool(7); //Plans to implement 7 threads
    private static long TASK_COUNT = 0;

    public static boolean debugging = true;

    /**
     * Initializes the important variables and steps for this Library to
     * function properly and efficiently.
     */
    public static void initialize(String cacheRoot) {
	try {
	    Attachments.attachActorUpdator(new ActorUpdateThread());
	    Attachments.attachTicker(new TickThread());
	    Attachments.attachEventManager(new EventManager());
	} catch (Exception e) {
	    System.err.println("Error initializing Core...");
	    e.printStackTrace();
	}

    }

    /**
     * Submits the specified {@code CoreThread} to the thread pool of this
     * {@code Core} class.
     * 
     * @param thread
     *            the thread to submit to the thread pool
     * @return the Future representing pending completion of the thread
     */
    public static Future<?> submitThread(CoreThread thread) {
	return threadPool.submit(Objects.requireNonNull(thread, "You cannot submit a Core Thread equal to null"));
    }

    /**
     * Submits the specified {@code Runnable} object to the thread pool of this
     * {@code Core} class. The priority of the submitted task is set to
     * {@link Thread#MIN_PRIORITY} and it is also set as a daemon thread.
     * 
     * <p>
     * This method is meant to execute any short and simple tasks that do not
     * stress the CPU or take much time. Tasks submitted using this method are
     * usually not as important as any other submitted using the
     * {@link Core#submitThread(CoreThread)} method.
     * 
     * @param r
     *            the task to submit to the thread pool
     * @return the Future representing pending completion of the task
     */
    public static Future<?> submitSimpleTask(Runnable r) {
	Thread thread = new Thread(r, "Simple Task[" + TASK_COUNT + "]");
	thread.setDaemon(true);
	thread.setPriority(Thread.MIN_PRIORITY);

	return threadPool.submit(Objects.requireNonNull(thread, "You cannot submit a task equal to null"));
    }

    /**
     * Submits the specified {@code Runnable} object to the thread pool of this
     * {@code Core} class. No changes are made to the task.
     * 
     * @param r
     *            the task to submit to the thread pool
     * @return the Future representing pending completion of the task
     */
    public static Future<?> submitRegularTask(Runnable r) {
	return threadPool.submit(r);
    }

    /**
     * Schedules the specified {@code thread} with the delay and period from the
     * given arguments based on the specified {@code TimeUnit}.
     * 
     * @param thread
     *            the thread to schedule
     * @param delay
     *            the delay before the schedule will be scheduled
     * @param period
     *            the period the thread is run for
     * @param unit
     *            the {@code TimeUnit} at which the delay and period are
     *            calculated
     * @return a {@code ScheduledFuture} representing pending completion of the
     *         task
     */
    public static ScheduledFuture<?> scheduleFixedTask(CoreThread thread, long delay, long period, TimeUnit unit) {
	return threadPool.scheduleAtFixedRate(Objects.requireNonNull(thread, "You cannot schedule a Core Thread to null"), delay, period, unit);
    }

}
