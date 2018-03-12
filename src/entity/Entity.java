package entity;

import util.ReflectUtil;
import util.configuration.ConfigSection;
import entity.geometry.EntityLocationChangeEvent;
import entity.geometry.Locatable;
import entity.geometry.Location;
import entity.geometry.map.AreaChangeType;

/**
 * @author Albert Beaupre
 */
public abstract class Entity implements Locatable {

	private ConfigSection temporary; // This value will be null until any
												// temporary variables are set
	private Location location;
	private int index;

	/**
	 * The {@code EntityOptions} placed on this {@code Entity}.
	 */
	public final EntityOptions options = new EntityOptions(this);

	/**
	 * Constructs a new {@code Entity} at the specified {@code Location}.
	 */
	public Entity() {}

	/**
	 * Sets the location of this {@code Entity} to the specified {@code Location}.
	 * 
	 * <p>
	 * It would be best to use this method when walking an {@code Entity} as such:
	 * 
	 * <pre>
	 * int walkX;
	 * int walkY;
	 * 
	 * setLocation(location.translate(walkX, walkY, 0), AreaChangeType.WALK);
	 * </pre>
	 * 
	 * @param location
	 *           the location to set this {@code Entity}
	 */
	public final void setLocation(Location location, AreaChangeType type) {
		Location previousLocation = this.location;
		this.location = location;

		EntityLocationChangeEvent event = new EntityLocationChangeEvent(this, type, this.location, previousLocation);
		event.call();

		this.location.map.load(location.x, location.y);
	}

	/**
	 * Sets the location of this {@code Entity} to the specified {@code Location}.
	 * 
	 * <p>
	 * This method is effectively equivalent to:
	 * 
	 * <pre>
	 * setLocation(location, {@link AreaChangeType#SERVER});
	 * </pre>
	 * 
	 * @param location
	 *           the location to set this {@code Entity}
	 */
	public final void setLocation(Location location) {
		this.setLocation(location, AreaChangeType.SERVER);
	}

	/**
	 * This will trigger the {@link entity.geometry.EntityLocationChangeEvent} event with the
	 * {@link AreaChangeType#TELEPORT} as the type of location change.
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
	 * @see entity.geometry.Locatable#getLocation()
	 */
	public final Location getLocation() {
		return location;
	}

	/**
	 * Selects the {@code EntityOption} on this {@code Entity} associated with the specified
	 * {@code text}.
	 * 
	 * @param text
	 *           the text of the option
	 */
	public final void selectOption(Entity interactor, String text) {
		interactor.options.select(text, this);
	}

	/**
	 * Selects the {@code EntityOption} on this {@code Entity} associated with the specified
	 * {@code index}.
	 * 
	 * @param index
	 *           the index of the option
	 */
	public final void selectOption(Entity interactor, int index) {
		interactor.options.select(index, this);
	}

	/**
	 * This method is called when this {@code Entity} is added to an {@code EntityList}. It is used
	 * to initialize this {@code Entity}.
	 * 
	 * @see entity.EntityList#add(Entity)
	 */
	public abstract void create();

	/**
	 * This method is called when this {@code Entity} is removed from an {@code EntityList}. It is
	 * used to remove any relating values in memory or unnecessary values that aren't worth keeping
	 * for this {@code Entity}.
	 * 
	 * @see entity.EntityList#remove(Entity)
	 * @see entity.EntityList#remove(int)
	 */
	public abstract void destroy();

	/**
	 * Returns the name of this {@code Entity}.
	 * 
	 * @return the name
	 */
	public abstract String getName();

	/**
	 * Returns the index value relating to an {@code EntityList} that this {@code Entity} is stored.
	 * If this {@code Entity} is not within an {@code EntityList}, its index value will return -1 by
	 * default.
	 * 
	 * @return index within {@code EntityList}; return -1 if not within {@code EntityList}.
	 */
	public final int getIndex() {
		return index;
	}

	/**
	 * This method will set a temporary variable to this {@code Entity}. If the value is set to null,
	 * then it will remove the temporary variable.
	 * 
	 * @param name
	 *           the name of the variable
	 * @param value
	 *           the value of the variable
	 */
	public void temporary(String name, Object value) {
		if (this.temporary == null)
			this.temporary = new ConfigSection();
		this.temporary.put(name, value);
		if (value == null)
			this.temporary.remove(name);

		if (this.temporary.isEmpty())
			this.temporary = null; // remove any unecessary memory
	}

	/**
	 * 
	 * 
	 * @param name
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getTemporary(String name, T fallback) {
		return this.temporary == null ? null : (T) this.temporary.get(name);
	}

	/**
	 * This method is used <b>only</b> by an {@code EntityList} instance to set the index of this
	 * {@code Entity}. If an {@code EntityList} instance is not used to call this method, then an
	 * {@code UnsupportedOpperationException} is thrown.
	 * 
	 * @param index
	 * 
	 * @throws UnsupportedOperationException
	 *            if a class calls this method that isn't instance of {@code EntityList}
	 */
	public final void setIndex(int index) {
		try {
			if (!ReflectUtil.getCallerClass(2).isAssignableFrom(EntityList.class))
				throw new UnsupportedOperationException("The index of an Entity must only be modified by an EntityList instance");
			this.index = index;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Entity[" + this.getName() + "]";
	}
}
