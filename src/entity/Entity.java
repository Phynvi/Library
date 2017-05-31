package entity;

import java.util.Objects;

import util.ReflectUtil;
import entity.geometry.Locatable;
import entity.geometry.Location;

/**
 * @author Albert Beaupre
 */
public abstract class Entity implements Locatable, Interactable {

    private final Location location;
    private Location previousLocation;

    private int index;

    /**
     * Constructs a new {@code Entity} at the specified {@code Location}.
     * 
     * @param location
     *            the location to create the entity at
     */
    public Entity(Location location) {
	this.location = Objects.requireNonNull(location, "The location of an entity must not be NULL");
	this.previousLocation = location;
    }

    /**
     * Sets the location of this {@code Entity} to the specified
     * {@code Location}.
     * 
     * @param location
     *            the location to set this {@code Entity}
     */
    public final void setLocation(Location location) {
	this.previousLocation = this.location;

	this.location.setMap(location.getMap());
	this.location.x = location.x;
	this.location.y = location.y;
	this.location.z = location.z;
    }

    public final Location getPreviousLocation() {
	return this.previousLocation;
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
    protected abstract void create();

    /**
     * This method is called when this {@code Entity} is removed from an
     * {@code EntityList}. It is used to remove any relating values in memory or
     * unnecessary values that aren't worth keeping for this {@code Entity}.
     * 
     * @see entity.EntityList#remove(Entity)
     * @see entity.EntityList#remove(int)
     */
    protected abstract void destroy();

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
