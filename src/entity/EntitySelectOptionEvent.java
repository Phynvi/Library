package entity;

import event.Event;

public class EntitySelectOptionEvent extends Event {

	public final Entity interactor;
	public final Entity target;
	public final EntityOption entityOption;

	public EntitySelectOptionEvent(Entity interactor, Entity target, EntityOption entityOption) {
		this.interactor = interactor;
		this.target = target;
		this.entityOption = entityOption;
	}

}
