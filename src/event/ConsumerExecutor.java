package event;

import java.util.function.Consumer;

public class ConsumerExecutor<T extends Event> extends EventExecutor {

	private final Consumer<T> consumer;
	
	public ConsumerExecutor(Consumer<T> consumer) {
		this.consumer = consumer;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(Event event) {
		this.consumer.accept((T) event);
	}
}
