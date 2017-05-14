package geometry;

/**
 * Represents a type of Object that can be locatable by a specific
 * {@code Point3D}.
 * 
 * @author Albert Beaupre
 *
 * @see geometry.Point3D
 */
public interface Locatable {

    public Point3D getLocation();

}
