package game.interfaces;

import java.util.ArrayList;

/**
 * Represents all of the currently active panes that the owner of this PaneSet
 * has open.
 * 
 * @author Albert Beaupre
 */
public abstract class PaneSet {

    /**
     * The currently open panes, where the last one in the list is the currently
     * active one. The rest are idle and inaccessible by ordinary clients.
     */
    private ArrayList<Pane> panes = new ArrayList<>(1);

    /**
     * 
     * @param pane
     * @param redraw
     */
    public abstract void sendWindowPane(int pane, boolean redraw);

    /**
     * 
     * @param clickable
     * @param parentId
     * @param position
     * @param interfaceId
     */
    public abstract void sendInterface(boolean clickable, int parentId, int position, int interfaceId);

    /**
     * 
     * @param window
     * @param position
     */
    public abstract void sendCloseInterface(int window, int position);

    /**
     * Sends the right click/use configuration for items/spells/prayers on this
     * interface. To convert dementhium to Blaze, dementhium is of the format:<br>
     * sendAMask(Player holder, int set1, int set2, int interfaceId1, int
     * childId1, int interfaceId2, int childId2)<br>
     * Where interfaceId2 << 16 | childId2 = is our int flags,<br>
     * set2 is our length,<br>
     * set1 is our offset,<br>
     * childId1 is our componentId<br>
     * 
     * @param flags
     *            The flags. Use a SettingsBuilder to generate these easily
     * @param offset
     *            the starting index of the containers items. Eg for inventory
     *            this is 0.
     * @param length
     *            the number of items. Eg for inventory this is 27
     * @param componentId
     *            the child ID of the interface to modify. This is frequently 0.
     */
    public abstract void setAccessMask(int interfaceId, int flags, int offset, int length, int componentId);

    /**
     * Sets the String overlay for the given component for this interface.
     * 
     * @param componentId
     *            the ID, these start at 0
     * @param s
     *            the String to set
     */
    public abstract void setString(int interfaceId, int componentId, String s);

    /**
     * Sets the specified component to be visible/invisible based on the
     * specified {@code visible} argument.
     * 
     * @param componentId
     *            the component to change visibility
     * @param visible
     *            the flag to set as visible or not
     */
    public abstract void setComponentVisible(int interfaceId, int componentId, boolean visible);

    /**
     * Adds the given Pane to this PaneSet. The given Pane is added to the top
     * of the list of panes, and the player is informed of the new pane. If the
     * pane is not yet visible, pane.open() is called on it.
     * 
     * @param pane
     *            the pane to open
     */
    public void add(Pane pane) {
	if (!pane.isOpen())
	    pane.onOpen();

	panes.add(pane); //This is our new active pane

	sendWindowPane(pane.getChildId(), false);
    }

    /**
     * Removes the given Pane from this PaneSet. If the Pane is visible,
     * pane.close() is called on it. If the given Pane is the last Pane in this
     * PaneSet, an IllegalStateException is thrown, as a client must always have
     * a Pane open. This method notifies the player through the protocol
     * 
     * @param pane
     *            the Pane to remove
     */
    public void remove(Pane pane) {
	if (panes.size() == 1 && panes.contains(pane))
	    throw new IllegalStateException("A player must always have an active pane after being instantiated. Removing the pane " + pane + " would mean they have no active panes.");

	panes.remove(pane);

	if (pane.isOpen())
	    pane.onClose();

	sendWindowPane(getActive().getChildId(), true);
    }

    /**
     * Fetches the currently active Pane to the player. A player may only have
     * one Pane active at a time.
     * 
     * @return The currently active Pane, or null if no pane has been added yet.
     */
    public Pane getActive() {
	if (panes.isEmpty())
	    return null;
	//The last pane is regarded as the active one.
	return panes.get(panes.size() - 1);
    }

    /**
     * Returns the {@code Window} with the associated {@code id}, if it exists;
     * otherwise null is returned.
     * 
     * @param id
     *            the id associated with the window
     * @return the window associated with the id
     */
    public Window get(int id) {
	Pane active = getActive();
	if (active == null)
	    return null;

	if (active.getChildId() == id)
	    return active;
	return active.getInterface(id);
    }
}