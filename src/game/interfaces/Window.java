package game.interfaces;

import java.util.Objects;

/**
 * @author Albert Beaupre
 */
public abstract class Window {

	/**
	 * The {@code PaneHolder} of this {@code Window}.
	 */
	protected PaneHolder holder;

	private int interfaceId = -1;

	/**
	 * Constructs a new {@code Window}.
	 * 
	 * @param holder
	 *            the pane holder holding the window
	 */
	public Window(PaneHolder holder) {
		this.holder = Objects.requireNonNull(holder, "A PaneHolder cannot equal NULL");
	}

	/**
	 * Sets the child id of this {@code Window} to the specified {@code id}, normally referred to as the
	 * interface id.
	 * 
	 * @return the child id
	 */
	public void setChildId(int id) {
		if (id < 0 || id > 0xFFFF)
			throw new IllegalArgumentException("InterfaceId must be a short, and therebefore between 0 and " + (0xFFFF));

		if (this.isOpen())
			throw new IllegalStateException("Interface is already open and therefore should not have its type ID modified");

		this.interfaceId = id;
	}

	/**
	 * Unlocks the specified {@code options} for the specified componentId with the specified offset and
	 * length.
	 * 
	 * @param offset
	 *            the starting index of the containers items. Eg for inventory this is 0.
	 * @param length
	 *            the number of items. Eg for inventory this is 27
	 * @param componentId
	 *            the child ID of the interface to modify. This is frequently 0.
	 * @param options
	 *            the options to unlock in this window
	 */
	public void setUnlockOptions(int offset, int length, int componentId, int... options) {
		int hash = 0;
		for (int o : options)
			hash |= 2 << o;
		holder.setAccessMask(this.interfaceId, hash, offset, length, componentId);
	}

	/**
	 * This method is called when this {@code Window} is opened.
	 */
	public void onOpen() {}

	/**
	 * This method is called when this {@code Window} is closed.
	 */
	public void onClose() {}

	/**
	 * Returns true if this {@code Window} is opened; returns false otherwise
	 * 
	 * @return true if open; return false otherwise
	 */
	public boolean isOpen() {
		return holder.getPanes().getActive() == this;
	}

	/**
	 * Called when the owner of this interface clicks on a button in this interface
	 * 
	 * @param entityOption
	 *            the entityOption number that was used. Eg, right click provides multiple options
	 * @param buttonId
	 *            the buttonId that was clicked
	 * @param slotId
	 *            the slot that was clicked, possibly 65536 (-1)
	 * @param itemId
	 *            the item ID that was clicked, possibly -1, not to be trusted.
	 */
	public abstract void onClick(int option, int buttonId, int slotId, int itemId);

	/**
	 * Returns the child id of this {@code Window}, normally referred to as the interface id.
	 * 
	 * @return the child id
	 */
	public int getChildId() {
		return interfaceId;
	}
}