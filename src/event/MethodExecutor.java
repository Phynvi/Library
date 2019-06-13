package event;

import java.lang.reflect.Method;

/**
 * Used to execute a {@code Method} when an {@code EventListener} has listened to the call of an
 * {@code Event}.
 * 
 * @author Albert
 */
public class MethodExecutor extends EventExecutor {
	
	private final EventListener listener;
	private final Method method;

	/**
	 * Constructs a new {@code EventExecutor} with the specified {@code listener} that uses the
	 * specified {@code method} for execution.
	 * 
	 * @param listener
	 *            the event listener with the underlying method
	 * @param method
	 *            the method to use for execution
	 */
	public MethodExecutor(EventListener listener, Method method) {
		this.listener = listener;
		this.method = method;
	}

	/**
	 * Executes the specified {@code event} using the {@code EventListener} and {@code Method} attached
	 * to this {@code EventExecutor.}
	 * 
	 * @param event
	 *            the event to execute
	 */
	@Override
	public void execute(Event event) {
		try {
			method.invoke(listener, event);
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}
	}

	@Override
	public boolean equals(Object obj) {
		return listener.equals(obj);
	}

	@Override
	public int hashCode() {
		return listener.hashCode();
	}

	@Override
	public String toString() {
		return listener.getClass().getSimpleName();
	}
}
