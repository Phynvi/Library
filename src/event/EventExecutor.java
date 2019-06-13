package event;

public abstract class EventExecutor {
	
	private EventPriority priority = EventPriority.NORMAL;
	
	public abstract void execute(Event event);
	
	public EventPriority getPriority() {
		return priority;
	}
	
	public void setPriority(EventPriority priority) {
		this.priority = priority;
	}
}
