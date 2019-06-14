package event.impl;

import entity.Entity;
import entity.EntityOption;
import event.Event;
import lombok.Getter;

@Getter
public class EntityChangeOptionEvent extends Event {

	private final Entity entity;
	private final EntityOption from;
	private final EntityOption to;

	public EntityChangeOptionEvent(Entity entity, EntityOption from, EntityOption to) {
		this.entity = entity;
		this.from = from;
		this.to = to;
	}
}
