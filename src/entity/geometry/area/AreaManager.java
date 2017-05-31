package entity.geometry.area;

import infrastructure.Attachments;

import java.util.HashSet;

import entity.geometry.Point3D;
import entity.geometry.Shape3D;

/**
 * The {@code AreaManager} holds
 * 
 * @author Albert Beaupre
 * 
 * @see entity.geometry.area.Area
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
}
