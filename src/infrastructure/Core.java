package infrastructure;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;

import timing.Ticker;
import event.EventManager;

/**
 * @author Albert Beaupre
 */
public class Core {

    private static ExecutorService threadPool;

    /**
     * Initializes the important variables and steps for this Library to
     * function properly and efficiently.
     */
    public static void initialize() {
	threadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors(), new ThreadFactory() {
	    private int nextThreadId = 0;

	    @Override
	    public Thread newThread(Runnable r) {
		Thread t = new Thread(r, "ExecutorService " + nextThreadId++);
		t.setPriority(Thread.NORM_PRIORITY);
		t.setContextClassLoader(ClassLoader.getSystemClassLoader());
		t.setDaemon(true);
		return t;
	    }
	});

	Attachments.attachTicker(new Ticker());
	Attachments.attachEventManager(new EventManager());
    }

    /**
     * Any {@code Runnable} task submitted will be put in a thread that is a
     * daemon thread. The reason for this is to have the ability to save and
     * manage server information when an unexpected shutdown has happened. The
     * {@code Runnable} task submitted will be placed within an
     * {@code ExecutorService} serving as a <b>Thread Pool</b>.
     * 
     * @param r
     *            the task to submit to the thread pool
     * @return the Future representing pending completion of the task
     */
    public static Future<?> submit(Runnable r) {
	return threadPool.submit(Objects.requireNonNull(r, "You cannot submit a task equal to null"));
    }

}
