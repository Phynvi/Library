package infrastructure;

import infrastructure.timing.Ticker;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import event.EventManager;

/**
 * TheAJ has very good information on threads for a RuneScape Private Server
 * 
 * https://www.rune-server.ee/runescape-development/rs-503-client-server/informative-threads/171504-multithreading.html
 */

/**
 * @author Albert Beaupre
 */
public class Core {

    private static ExecutorService threadPool;
    private static int TASK_COUNT = 0;

    /**
     * Initializes the important variables and steps for this Library to
     * function properly and efficiently.
     */
    public static void initialize() {
	threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

	Attachments.attachTicker(new Ticker());
	Attachments.attachEventManager(new EventManager());
    }

    /**
     * Submits the specified {@code CoreThread} to the thread pool of this
     * {@code Core} class.
     * 
     * @param r
     *            the task to submit to the thread pool
     * @return the Future representing pending completion of the task
     */
    public static Future<?> submit(CoreThread r) {
	return threadPool.submit(Objects.requireNonNull(r, "You cannot submit a Core Thread equal to null"));
    }

    /**
     * Submits the specified {@code Runnable} object to the thread pool of this
     * {@code Core} class. The priority of the submitted task is equal to
     * {@link Thread#MIN_PRIORITY} and it is set as a daemon thread.
     * 
     * @param r
     *            the task to submit to the thread pool
     * @return the Future representing pending completion of the task
     */
    public static Future<?> submit(Runnable r) {
	Thread thread = new Thread(r, "Task " + TASK_COUNT);
	thread.setDaemon(true);
	thread.setPriority(Thread.MIN_PRIORITY);

	return threadPool.submit(Objects.requireNonNull(thread, "You cannot submit a task equal to null"));
    }

}
