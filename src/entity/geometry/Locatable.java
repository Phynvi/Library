package entity.geometry;

/**
 * Represents a type of Object that can be locatable by a specific {@code Location}.
 * 
 * @author Albert Beaupre
 *
 * @see entity.geometry.Location
 */
public interface Locatable {

	/**
	 * Returns the {@code Location} of this {@code Locatable} object.
	 * 
	 * @return the {@code Location}
	 */
	public Location getLocation();

	/**
	 * Sets the location for this {@code Locatable} object.
	 * 
	 * @param location
	 *            the location to set to
	 */
	public void setLocation(Location location);

}
