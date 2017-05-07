package event;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 * <p>
 * Holds {@code EventListener} implementations to listen for {@code Event} calls
 * that are relevant to them. Once an {@code EventListener} has been registered,
 * by the {@link EventManager#registerEventListener(EventListener)} method, any
 * method annotated by the {@code EventMethod} annotation will be registered to
 * listening to its relevant {@code Event}. If an {@code EventListener} has
 * successfully listened to an {@code Event}, any method that has been
 * registered within the listener will be called.
 * 
 * @see event.Event
 * @see event.EventListener
 * @see event.EventMethod
 * 
 * @author Albert Beaupre
 *
 * @version 1.0
 */
public class EventManager {

    /**
     * Used to execute a {@code Method} when an {@code EventListener} has
     * listened to the call of an {@code Event}.
     * 
     * @author Albert
     */
    private class EventExecutor {

	private final EventListener listener;
	private final Method method;

	/**
	 * Constructs a new {@code EventExecutor} with the specified
	 * {@code listener} that uses the specified {@code method} for
	 * execution.
	 * 
	 * @param listener
	 *            the event listener with the underlying method
	 * @param method
	 *            the method to use for execution
	 */
	public EventExecutor(EventListener listener, Method method) {
	    this.listener = listener;
	    this.method = method;
	}

	/**
	 * Executes the specified {@code event} using the {@code EventListener}
	 * and {@code Method} attached to this {@code EventExecutor.}
	 * 
	 * @param event
	 *            the event to execute
	 */
	public void execute(Event event) {
	    try {
		method.invoke(listener, event);
	    } catch (Throwable throwable) {
		throwable.printStackTrace();
	    }
	}

	@Override
	public boolean equals(Object obj) {
	    return true;
	}

	@Override
	public int hashCode() {
	    return listener.hashCode();
	}

	@Override
	public String toString() {
	    return listener.toString();
	}

    }

    /**
     * This map is used to store event listener methods based on their relevant
     * event.
     */
    private final HashMap<Class<? extends Event>, HashSet<EventExecutor>> eventExecutors;

    /**
     * Constructs a new {@code EventManager} with no {@code EventListener}
     * registered.
     */
    public EventManager() {
	this.eventExecutors = new HashMap<>();
    }

    /**
     * Registers the specified {@code listener} to this {@code EventManager} to
     * listen for any events relevant to the methods the {@code listener} has.
     * 
     * @param listener
     *            the listener to be registered
     * 
     * @see event.EventListener
     */
    public void registerEventListener(EventListener listener) {
	for (Method method : listener.getClass().getMethods()) {
	    if (method.getParameterTypes().length != 1)
		continue;
	    if (!method.isAnnotationPresent(EventMethod.class))
		continue;
	    method.setAccessible(true);
	    @SuppressWarnings("unchecked")
	    Class<? extends Event> eventClazz = (Class<? extends Event>) method.getParameterTypes()[0];
	    HashSet<EventExecutor> methodSet = eventExecutors.getOrDefault(eventClazz, new HashSet<>());
	    methodSet.add(new EventExecutor(listener, method));
	    eventExecutors.put(eventClazz, methodSet);
	}
    }

    /**
     * Unregisters the specified {@code listener} from this {@code EventManager}
     * if it is existing, so it cannot listen for any events.
     * 
     * @param listener
     *            the listener to be unregistered, if existing
     * 
     * @see event.EventListener
     */
    public void unregisterEventListener(EventListener listener) {
	for (HashSet<EventExecutor> set : eventExecutors.values()) {
	    for (EventExecutor executor : set) {
		if (executor.equals(listener)) {
		    set.remove(executor);
		}
	    }
	}
    }

    /**
     * Calls the specified {@code event} so that any registered
     * {@code EventListener} will listen for the call and be executed upon
     * receiving the call.
     * 
     * @param event
     *            the event to be called for listening
     * 
     * @see event.Event
     */
    public void callEvent(Event event) {
	for (Entry<Class<? extends Event>, HashSet<EventExecutor>> entry : eventExecutors.entrySet()) {
	    if (entry.getKey().isInstance(event)) {
		for (EventExecutor executor : entry.getValue()) {
		    if (event.isCancelled())
			continue;
		    executor.execute(event);
		    if (event.isConsumed())
			break;
		}
	    }
	}
    }
}
