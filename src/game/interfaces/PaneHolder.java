package game.interfaces;

import container.Container;
import container.Item;
import entity.actor.persona.Persona;

/**
 * 
 * @author Albert Beaupre
 */
public interface PaneHolder {

	/**
	 * Returns the {@code PaneSet} of this {@code PaneHolder}.
	 * 
	 * @return the panes
	 */
	public PaneSet getPanes();

	/**
	 * Returns the {@code Pane} which is used as the main game pane.
	 * 
	 * @return the main game pane
	 */
	public Pane getGamePane();

	public abstract void sendItemOnInterface(int interfaceId, int childId, int itemId);

	public abstract void sendGlobalString(int id, String string);

	public abstract void invokeBlankScript(int id);

	public abstract void invokeScript(int id, Object... params);

	public abstract void sendGlobalConfig(int id, int value);

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
	 * Sends the right click/use configuration for items/spells/prayers on this interface. To convert
	 * dementhium to Blaze, dementhium is of the format:<br>
	 * sendAMask(Player holder, int set1, int set2, int interfaceId1, int childId1, int interfaceId2,
	 * int childId2)<br>
	 * Where interfaceId2 << 16 | childId2 = is our int flags,<br>
	 * set2 is our length,<br>
	 * set1 is our offset,<br>
	 * childId1 is our componentId<br>
	 * 
	 * @param flags
	 *            The flags. Use a SettingsBuilder to generate these easily
	 * @param offset
	 *            the starting index of the containers items. Eg for inventory this is 0.
	 * @param length
	 *            the number of items. Eg for inventory this is 27
	 * @param componentId
	 *            the child ID of the interface to modify. This is frequently 0.
	 */
	public abstract void sendAccessMask(int interfaceId, int flags, int offset, int length, int componentId);

	/**
	 * Sets the String overlay for the given component for this interface.
	 * 
	 * @param componentId
	 *            the ID, these start at 0
	 * @param s
	 *            the String to set
	 */
	public abstract void sendString(int interfaceId, int componentId, String s);

	/**
	 * Sets the specified component to be visible/invisible based on the specified {@code visible}
	 * argument.
	 * 
	 * @param componentId
	 *            the component to change visibility
	 * @param visible
	 *            the flag to set as visible or not
	 */
	public abstract void sendComponentVisible(int interfaceId, int componentId, boolean visible);

	/**
	 * 
	 * @param interfaceId
	 * @param split
	 * @param container
	 */
	public abstract void sendItems(int interfaceId, boolean split, Container<? extends Item> container);

	public abstract void sendConfig(int id, int value);

	public abstract void sendNPCOnInterface(int interfaceId, int componentId, int npcId);

	public abstract void sendPlayerOnInterface(Persona persona, int interfaceId, int componentId);

	public abstract void sendAnimationOnInterface(int interfaceId, int componentId, int animation);
}
