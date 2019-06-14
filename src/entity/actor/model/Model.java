package entity.actor.model;

import infrastructure.GlobalVariables;

/**
 * @author Albert Beaupre
 */
public abstract class Model {

	private final Mask[] currentMasks;
	private boolean updating;
	private byte maskCount = 0;
	private short maskData;

	/**
	 * Constructs a new {@code Model}.
	 */
	public Model(int maskSize) {
		this.currentMasks = new Mask[maskSize];
	}

	/**
	 * This method is used for updating any essential variables for this {@code Model} to use before it
	 * is reset.
	 */
	public abstract void update();

	/**
	 * Resets any variables used by the {@link Model#update()} method to be re-initialized.
	 */
	public abstract void reset();

	/**
	 * Returns true if this {@code Model} can be updated at all; return false otherwise. This method
	 * will usually check if the {@code Model} is online or visible etc.
	 * 
	 * @return true if can be updated; return false otherwise
	 */
	public abstract boolean canBeUpdated();

	/**
	 * Registers the specified {@code mask} to be updated for this {@code Model}
	 * 
	 * @param mask
	 *            the mask to register
	 */
	public void registerMask(Mask mask) {
		this.currentMasks[mask.ordinal()] = mask;
		this.maskData |= mask.data();
		this.maskCount++;
		if (!this.updating)
			setForUpdating(true);
	}

	/**
	 * Returns a new {@code TreeSet} filled with the current masks of this {@code Model}.
	 * 
	 * @return the current masks
	 */
	public Mask[] getMasks() {
		return this.currentMasks;
	}

	/**
	 * Returns the mask correlating to the given {@code ordinal}.
	 * 
	 * @param ordinal
	 *            the correlating ordinal of the mask
	 * @return the mask relating to the ordinal
	 */
	public Mask getMask(int ordinal) {
		return this.currentMasks[ordinal];
	}

	/**
	 * Finishes updating the masks of this {@code Model}.
	 */
	public final void finishUpdate() {
		reset();
		this.maskData = 0;
		this.maskCount = 0;
		for (byte i = 0; i < this.currentMasks.length; i++)
			this.currentMasks[i] = null;
	}

	/**
	 * Checks if an update is required.
	 * 
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isUpdateRequired() {
		return this.maskData > 0 || maskCount > 0;
	}

	/**
	 * Checks if a mask was registered based on the given update mask data.
	 * 
	 * @param data
	 *            The mask data of the update flag.
	 * @return true if the mask was registered, false if not.
	 */
	public boolean activated(short data) {
		return (this.maskData & data) != 0;
	}

	/**
	 * Checks if a mask was registered based on the given ordinal
	 * 
	 * @param ordinal
	 *            the ordinal of the mask
	 * @return true if the mask was registered, false if not.
	 */
	public boolean activated(byte ordinal) {
		return this.currentMasks[ordinal] != null;
	}

	/**
	 * Returns the combined mask data of this {@code Model}.
	 * 
	 * @return the combined mask data
	 */
	public short getMaskData() {
		return this.maskData;
	}

	/**
	 * Sets this {@code Model} to be updated based on the given {@code setForUpdating} flag.
	 * 
	 * @param setForUpdating
	 *            the flag for having this model update
	 */
	public void setForUpdating(boolean setForUpdating) {
		this.updating = setForUpdating;
		if (setForUpdating)
			GlobalVariables.getModelUpdater().setForUpdating(this);
	}
}
