package event.impl;

import entity.Entity;
import entity.EntityOption;
import event.Event;
import lombok.Getter;

@Getter
public class EntitySelectOptionEvent extends Event {

	private final Entity interactor;
	private final Entity target;
	private final EntityOption entityOption;

	public EntitySelectOptionEvent(Entity interactor, Entity target, EntityOption entityOption) {
		this.interactor = interactor;
		this.target = target;
		this.entityOption = entityOption;
	}
}
