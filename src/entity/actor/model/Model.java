package entity.actor.model;

import infrastructure.Attachments;
import java.util.TreeSet;
import entity.actor.Actor;

/**
 * @author Albert Beaupre
 */
public abstract class Model {

	private final TreeSet<Mask> currentMasks;
	private int maskData;

	/**
	 * The {@code Actor} used within this {@code Model} for updating.
	 */
	protected final Actor actor;

	/**
	 * Constructs a new {@code Model}.
	 */
	public Model(Actor actor) {
		this.actor = actor;
		this.currentMasks = new TreeSet<>();
	}

	/**
	 * This method is used for updating any essential variables for this {@code Model} to use before
	 * it is reset.
	 */
	public abstract void update();

	/**
	 * Resets any variables used by the {@link Model#update()} method to be re-initialized.
	 */
	public abstract void reset();

	/**
	 * Returns true if this {@code Model} can be updated at all; return false otherwise. This method
	 * will usually check if the {@code Actor} of this {@code Model} is online or invisible etc.
	 * 
	 * @return true if can be updated; return false otherwise
	 */
	public abstract boolean canBeUpdated();

	/**
	 * Registers the specified {@code mask} to be updated for this {@code Model}
	 * 
	 * @param mask
	 *           the mask to register
	 */
	public void registerMask(Mask mask) {
		if ((maskData & mask.data()) != 0)
			currentMasks.remove(mask);
		maskData |= mask.data();
		currentMasks.add(mask);

		Attachments.getActorUpdator().setForUpdating(actor);
	}

	/**
	 * Returns a new {@code TreeSet} filled with the current masks of this {@code Model}.
	 * 
	 * @return the current masks
	 */
	public TreeSet<Mask> getCurrentMasks() {
		return new TreeSet<>(this.currentMasks);
	}

	/**
	 * Finishes updating the masks of this {@code Model}.
	 */
	public void finishUpdate() {
		maskData = 0;
		currentMasks.clear();
	}

	/**
	 * Checks if an update is required.
	 * 
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isUpdateRequired() {
		return maskData != 0;
	}

	/**
	 * Checks if an update flag was registered.
	 * 
	 * @param data
	 *           The mask data of the update flag.
	 * @return {@code True} if the update flag was registered, {@code false} if not.
	 */
	public boolean activated(int data) {
		return (maskData & data) != 0;
	}

	/**
	 * Gets the mask data.
	 * 
	 * @return The mask data.
	 */
	public int getMaskData() {
		return maskData;
	}
}
