package entity;

import event.Event;

public class EntityChangeOptionEvent extends Event {

	public final Entity entity;
	public final EntityOption from;
	public final EntityOption to;

	public EntityChangeOptionEvent(Entity entity, EntityOption from, EntityOption to) {
		this.entity = entity;
		this.from = from;
		this.to = to;
	}

}
