package entity;

import util.ReflectUtil;
import entity.event.EntityLocationChangeEvent;
import entity.geometry.Locatable;
import entity.geometry.Location;
import entity.geometry.map.AreaChangeType;
import entity.interactable.Interactable;

/**
 * @author Albert Beaupre
 */
public abstract class Entity implements Locatable, Interactable {

    private Location location;
    private int index;

    /**
     * Constructs a new {@code Entity} at the specified {@code Location}.
     * 
     * @param location
     *            the location to create the entity at
     */
    public Entity() {}

    /**
     * Sets the location of this {@code Entity} to the specified
     * {@code Location}.
     * 
     * @param location
     *            the location to set this {@code Entity}
     */
    public final void setLocation(Location location) {
	this.location = location;
	this.location.getMap().load(location.x, location.y);
    }

    /**
     * Sets the location of this {@code Entity} to the specified
     * {@code Location}.
     * 
     * <p>
     * It would be best to use this method when walking an {@code Entity} as
     * such:
     * 
     * <pre>
     * int walkX;
     * int walkY;
     * 
     * setLocation(location.translate(walkX, walkY, 0), AreaChangeType.WALK);
     * </pre>
     * 
     * @param location
     *            the location to set this {@code Entity}
     */
    public final void setLocation(Location location, AreaChangeType type) {
	Location previousLocation = this.location;
	this.location = location;

	EntityLocationChangeEvent event = new EntityLocationChangeEvent(this, type, this.location, previousLocation);
	event.call();

	this.location.getMap().load(location.x, location.y);
    }

    /**
     * This will trigger the {@link entity.event.EntityLocationChangeEvent}
     * event with the {@link AreaChangeType#TELEPORT} as the type of location
     * change.
     * 
     * <p>
     * This method is effectively equivalent to:
     * 
     * <pre>
     * setLocation(location, {@link AreaChangeType#TELEPORT});
     * </pre>
     * 
     * @param location
     */
    public void teleport(Location location) {
	setLocation(location, AreaChangeType.TELEPORT);
    }

    /*
     * (non-Javadoc)
     * 
     * @see entity.geometry.Locatable#getLocation()
     */
    public final Location getLocation() {
	return location;
    }

    /**
     * This method is called when this {@code Entity} is added to an
     * {@code EntityList}. It is used to initialize this {@code Entity}.
     * 
     * @see entity.EntityList#add(Entity)
     */
    public abstract void create();

    /**
     * This method is called when this {@code Entity} is removed from an
     * {@code EntityList}. It is used to remove any relating values in memory or
     * unnecessary values that aren't worth keeping for this {@code Entity}.
     * 
     * @see entity.EntityList#remove(Entity)
     * @see entity.EntityList#remove(int)
     */
    public abstract void destroy();

    /**
     * Returns the index value relating to an {@code EntityList} that this
     * {@code Entity} is stored. If this {@code Entity} is not within an
     * {@code EntityList}, its index value will return -1 by default.
     * 
     * @return index within {@code EntityList}; return -1 if not within
     *         {@code EntityList}.
     */
    public final int getIndex() {
	return index;
    }

    /**
     * This method is used <b>only</b> by an {@code EntityList} instance to set
     * the index of this {@code Entity}. If an {@code EntityList} instance is
     * not used to call this method, then an
     * {@code UnsupportedOpperationException} is thrown.
     * 
     * @param index
     * 
     * @throws UnsupportedOperationException
     *             if a class calls this method that isn't instance of
     *             {@code EntityList}
     */
    public final void setIndex(int index) {
	try {
	    if (!ReflectUtil.getCallerClass(2).isAssignableFrom(EntityList.class))
		throw new UnsupportedOperationException("The index of an Entity must only be modified by an EntityList instance");
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	}
	this.index = index;
    }
}
