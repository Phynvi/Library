package infrastructure;

/**
 * The {@code CoreThread} class is a 'categorizer' class meant for modulating any Thread for the
 * server.
 * 
 * <p>
 * <b>DO NOT RUN EVERYTHING ON ONE THREAD</b> - Putting every action, task, event, and more on one
 * thread will put too much stress on your server performance.
 * 
 * @author Albert Beaupre
 */
public abstract class CoreThread extends Thread implements Runnable {

	/**
	 * Any {@code Thread} created as a {@code CoreThread} must contain these specified arguments.
	 * 
	 * @param name
	 *            the name of the thread
	 * @param priority
	 *            the priority type of the thread
	 * @param daemon
	 *            if the thread is a daemon type
	 */
	public CoreThread(String name, int priority, boolean daemon) {
		super.setName(name);
		super.setPriority(priority);
		super.setDaemon(daemon);

		super.setContextClassLoader(ClassLoader.getSystemClassLoader());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public abstract void run();

}
