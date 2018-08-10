package entity.geometry.map;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import entity.geometry.Point3D;
import entity.geometry.Shape3D;
import infrastructure.GlobalVariables;

/**
 * The {@code AreaManager} holds
 * 
 * @author Albert Beaupre
 * 
 * @see entity.geometry.map.Area
 */
public class AreaManager implements Shape3D {

	/**
	 * A new set is constructed every time this set is needing access because of better efficiency
	 * against concurrent sets.
	 */
	private HashSet<Area> combinedArea;

	/**
	 * Constructs a new {@code AreaManager} with empty arguments.
	 */
	public AreaManager() {
		this.combinedArea = new HashSet<>();
	}

	/**
	 * Combines the specified {@code Area} to this {@code AreaManager} to manage it.
	 * 
	 * @param area
	 *            the {@code Area} to combine to this {@code AreaManager}
	 */
	public void combine(Area area) {
		this.combinedArea.add(area);

		GlobalVariables.getEventManager().registerEventListener(area);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see geometry.Shape3D#contains(geometry.Point3D)
	 */
	public boolean contains(Point3D point) {
		HashSet<Area> newSet = new HashSet<>(combinedArea);

		for (Area area : newSet)
			if (area.contains(point))
				return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see entity.geometry.Shape3D#listPoints()
	 */
	public List<Point3D> listPoints() {
		List<Point3D> allPoints = new ArrayList<>();
		HashSet<Area> newSet = new HashSet<>(combinedArea);
		for (Area area : newSet)
			allPoints.addAll(area.listPoints());
		return allPoints;
	}

	@Override
	public boolean inRange(Point3D point, int range) {
		HashSet<Area> newSet = new HashSet<>(combinedArea);

		for (Area area : newSet)
			if (!area.inRange(point, range))
				return false;
		return true;
	}
}
