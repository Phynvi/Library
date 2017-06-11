package entity.event;

import entity.Entity;
import entity.geometry.Location;
import entity.geometry.area.AreaChangeType;
import event.Event;

public class EntityLocationChangeEvent extends Event {

    private final Entity entity;
    private final AreaChangeType type;
    private final Location currentLocation, previousLocation;

    public EntityLocationChangeEvent(Entity entity, AreaChangeType type, Location current, Location previous) {
	this.entity = entity;
	this.type = type;
	this.currentLocation = current;
	this.previousLocation = previous;
    }

    public Entity getEntity() {
	return entity;
    }

    public AreaChangeType getType() {
	return type;
    }

    public Location getCurrentLocation() {
	return currentLocation;
    }

    public Location getPreviousLocation() {
	return previousLocation;
    }
}
