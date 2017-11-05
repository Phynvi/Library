package entity.geometry;

import entity.Entity;
import entity.geometry.map.AreaChangeType;
import event.Event;

public class EntityLocationChangeEvent extends Event {

	public final Entity entity;
	public final AreaChangeType type;
	public final Location currentLocation, previousLocation;

	public EntityLocationChangeEvent(Entity entity, AreaChangeType type, Location current, Location previous) {
		this.entity = entity;
		this.type = type;
		this.currentLocation = current;
		this.previousLocation = previous;
	}
}
