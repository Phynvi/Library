package entity.actor.npc;

import entity.Entity;
import entity.actor.ActionQueue;
import entity.actor.Actor;
import entity.actor.model.Model;
import entity.geometry.Location;
import entity.geometry.path.Mobile;
import game.loot.DropTableHolder;

/**
 * 
 * @author Albert Beaupre
 */
public abstract class NPC extends Entity implements Actor, DropTableHolder, Mobile {

	private final ActionQueue<NPC> actions = new ActionQueue<>();

	private final int id;

	/**
	 * Constructs a new {@code NPC} located at the specified {@code location}.
	 * 
	 * @param location
	 *            the location to create this {@code NPC} at.
	 */
	public NPC(int id, Location location) {
		super.setLocation(location);
		this.id = id;
	}

	public NPC(int id) {
		this.id = id;
	}

	/**
	 * Returns the {@code Model} of this {@code Actor}.
	 * 
	 * @return the model
	 */
	public abstract Model getModel();

	/*
	 * (non-Javadoc)
	 * 
	 * @see entity.actor.Actor#getActions()
	 */
	public ActionQueue<NPC> getActions() {
		return actions;
	}

	public int getId() {
		return id;
	}

}
