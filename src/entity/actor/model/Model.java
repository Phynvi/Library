package entity.actor.model;

import infrastructure.Attachments;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

import entity.actor.Actor;

/**
 * @author Albert Beaupre
 */
public abstract class Model {

    private final TreeSet<Mask> currentMasks;
    private int maskData;

    private final Actor actor;

    /**
     * Constructs a new {@code Model}.
     */
    public Model(Actor actor) {
	this.actor = actor;
	this.currentMasks = new TreeSet<>();
    }

    /**
     * This method is used for updating any essential variables for this
     * {@code Model} to use before it is reset.
     */
    public abstract void update();

    /**
     * Resets any variables used by the {@link Model#update()} method to be
     * re-initialized.
     */
    public abstract void reset();

    /**
     * Registers the specified {@code mask} to be updated for this {@code Model}
     * 
     * @param mask
     *            the mask to register
     */
    public void registerMask(Mask mask) {
	if ((maskData & mask.data()) != 0)
	    currentMasks.remove(mask);
	maskData |= mask.data();
	currentMasks.add(mask);

	Attachments.getActorUpdator().setForUpdating(actor);
    }

    /**
     * Returns an unmodifiable {@code Collection} of the current masks within
     * this {@code Model}.
     * 
     * @return the unmodifiable collection of current masks
     */
    public Collection<Mask> getCurrentMasks() {
	return Collections.unmodifiableCollection(currentMasks);
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
     *            The mask data of the update flag.
     * @return {@code True} if the update flag was registered, {@code false} if
     *         not.
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

    /**
     * Returns the {@code Actor} using this {@code Model}.
     * 
     * @return the actor
     */
    public Actor getActor() {
	return actor;
    }
}
