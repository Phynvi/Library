package entity.event;

import entity.Entity;
import entity.geometry.area.AreaChangeType;
import event.Event;

public class EntityLocationChangeEvent extends Event {

    private final Entity entity;
    private final AreaChangeType type;

    public EntityLocationChangeEvent(Entity entity, AreaChangeType type) {
	this.entity = entity;
	this.type = type;
    }

    public Entity getEntity() {
	return entity;
    }

    public AreaChangeType getType() {
	return type;
    }
}
