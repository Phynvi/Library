package event.impl;

import entity.Entity;
import entity.geometry.Location;
import entity.geometry.map.AreaChangeType;
import event.Event;
import lombok.Getter;

@Getter
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
}
