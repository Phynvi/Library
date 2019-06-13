package event;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * <p>
 * Holds {@code EventListener} implementations to listen for {@code Event} calls that are relevant
 * to them. Once an {@code EventListener} has been registered, by the
 * {@link EventManager#registerEventListener(EventListener)} method, any method annotated by the
 * {@code EventMethod} annotation will be registered to listening to its relevant {@code Event}. If
 * an {@code EventListener} has successfully listened to an {@code Event}, any method that has been
 * registered within the listener will be called.
 * 
 * @see event.Event
 * @see event.EventListener
 * @see event.EventMethod
 * 
 * @author Albert Beaupre
 */
public class EventManager {

	/**
	 * This map is used to store event listener methods based on their relevant event.
	 */
	private final ConcurrentHashMap<Class<? extends Event>, HashSet<EventExecutor>> eventExecutors;

	/**
	 * Constructs a new {@code EventManager} with no {@code EventListener} registered.
	 */
	public EventManager() {
		this.eventExecutors = new ConcurrentHashMap<>();
	}

	/**
	 * Registers the specified {@code listener} to this {@code EventManager} to listen for any events
	 * relevant to the methods the {@code listener} has.
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
			MethodExecutor methodExecutor = new MethodExecutor(listener, method);
			methodExecutor.setPriority(method.getAnnotation(EventMethod.class).priority());
			methodSet.add(methodExecutor);
			eventExecutors.put(eventClazz, methodSet);
		}
	}
	
	public <T extends Event> void registerEvent(Class<T> eventClazz, Consumer<T> consumer) {
		registerEvent(eventClazz, consumer, EventPriority.NORMAL);
	}
	
	public <T extends Event> void registerEvent(Class<T> eventClazz, Consumer<T> consumer, EventPriority priority) {
		HashSet<EventExecutor> consumerSet = eventExecutors.getOrDefault(eventClazz, new HashSet<>());
		ConsumerExecutor<T> consumerExecutor = new ConsumerExecutor<>(consumer);
		consumerExecutor.setPriority(priority);
		consumerSet.add(consumerExecutor);
		eventExecutors.put(eventClazz, consumerSet);
	}

	/**
	 * Unregisters the specified {@code listener} from this {@code EventManager} if it is existing, so
	 * it cannot listen for any events.
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
	 * Calls the specified {@code event} so that any registered {@code EventListener} will listen for
	 * the call and be executed upon receiving the call.
	 * 
	 * @param event
	 *            the event to be called for listening
	 * 
	 * @see event.Event
	 */
	public void callEvent(Event event) {
		List<EventExecutor> executors = eventExecutors.get(event.getClass()).stream().sorted((e1, e2) ->  e2.getPriority().ordinal() - e1.getPriority().ordinal()).collect(Collectors.toList());
		for(EventExecutor e : executors) {
			if(!event.isCancelled()) {
				e.execute(event);
			}
			if (event.isConsumed()) {
				break;
			}
		}
	}
}
