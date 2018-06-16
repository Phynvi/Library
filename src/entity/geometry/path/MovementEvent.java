package entity.geometry.path;

import entity.geometry.Point3D;
import event.Event;

public class MovementEvent extends Event {

	private final Mobile mobile;
	private final Point3D from;
	private final Point3D to;

	public MovementEvent(Mobile mobile, Point3D from, Point3D to) {
		this.mobile = mobile;
		this.from = from;
		this.to = to;
	}

	public Mobile getMobile() {
		return mobile;
	}

	public Point3D getFrom() {
		return from;
	}

	public Point3D getTo() {
		return to;
	}

}
