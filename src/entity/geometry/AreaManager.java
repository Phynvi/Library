package entity.geometry;

import infrastructure.Attachments;

import java.util.HashSet;

/**
 * The {@code AreaManager} holds
 * 
 * @author Albert Beaupre
 * 
 * @see entity.geometry.Area
 */
public class AreaManager implements Shape3D {

    private HashSet<Area> combinedArea;

    /**
     * Constructs a new {@code AreaManager} with empty arguments.
     */
    public AreaManager() {
	this.combinedArea = new HashSet<>();
    }

    /**
     * Combines the specified {@code Area} to this {@code AreaManager} to manage
     * it.
     * 
     * @param area
     *            the {@code Area} to combine to this {@code AreaManager}
     */
    public void combine(Area area) {
	this.combinedArea.add(area);

	Attachments.getEventManager().registerEventListener(area);
    }

    /*
     * (non-Javadoc)
     * 
     * @see geometry.Shape3D#contains(geometry.Point3D)
     */
    public boolean contains(Point3D point) {
	for (Area shape : combinedArea)
	    if (shape.contains(point))
		return true;
	return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see geometry.Shape3D#intersects(geometry.Shape3D)
     */
    public boolean intersects(Shape3D shape) {
	for (Area s : combinedArea)
	    if (s.intersects(shape))
		return true;
	return false;
    }
}
