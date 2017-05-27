package entity.actor.model;

import java.util.Collection;
import java.util.Collections;
import java.util.TreeSet;

/**
 * @author Albert Beaupre
 */
public abstract class Model {

    private final TreeSet<UpdateMask> currentMasks;
    private int maskData;

    /**
     * Constructs a new {@code Model}.
     */
    public Model() {
	this.currentMasks = new TreeSet<>();
    }

    public abstract void update();

    /**
     * Registers the specified {@code mask} to be updated for this {@code Model}
     * .
     * 
     * @param mask
     *            the flag to register
     */
    public void registerMask(UpdateMask mask) {
	if ((maskData & mask.data()) != 0)
	    currentMasks.remove(mask);
	maskData |= mask.data();
	currentMasks.add(mask);
    }

    /**
     * Returns an unmodifiable {@code Collection} of the current masks within
     * this {@code Model}.
     * 
     * @return the unmodifiable collection of current masks
     */
    public Collection<UpdateMask> getCurrentMasks() {
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
    public boolean get(int data) {
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
