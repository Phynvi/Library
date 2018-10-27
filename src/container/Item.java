package container;

import util.DataCenter;
import util.configuration.ConfigSection;

/**
 * Represents an item with a specific id used within a {@link container.Container}. A {@code Item}
 * can have a modifiable amount, but not a modifiable id.
 * 
 * @author Albert Beaupre
 */
public class Item {

	private final short id; // This value should not be modifiable
	private int amount;

	private ConfigSection attributes;

	/**
	 * Constructs a new {@code Item} from the specified arguments.
	 * 
	 * @param id
	 *            the id of the item
	 * @param amount
	 *            the amount of the item
	 */
	public Item(int id, int amount) {
		this.id = (short) id;
		this.amount = amount;
	}

	/**
	 * Constructs a new {@code Item} from the specified arguments.
	 * 
	 * @param id
	 *            the id of the item
	 */
	public Item(int id) {
		this(id, 1);
	}

	/**
	 * Returns the id of this {@code Item}.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the amount of this {@code Item}.
	 * 
	 * @return the amount
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Sets an attribute to this {@code Item} with the specified {@code attributeName}.
	 * 
	 * @param attributeName
	 *            the name of the attribute
	 * @param value
	 *            the value of the attribute
	 */
	public void attribute(String attributeName, Object value) {
		attributeName = attributeName.toLowerCase();

		if (attributes == null)
			attributes = new ConfigSection();
		if (attributes.containsKey(attributeName)) {
			if (value == null)
				attributes.remove(attributeName);
		} else {
			attributes.put(attributeName, value);
		}
		if (attributes.isEmpty())
			attributes = null; //There are so many items within the game, this needs to be disposed of
	}

	/**
	 * Returns a new {@code Item} with the same information but with amount of the item decreased by the
	 * given argument.
	 * 
	 * @param amount
	 *            the amount to set
	 * @return the new {@code Item} with the decreased {@code amount}
	 */
	public Item decrease(int amount) {
		return new Item(id, this.amount - amount);
	}

	/**
	 * Returns a new {@code Item} with the same information but with amount of the item increased by the
	 * given argument.
	 * 
	 * @param amount
	 *            the amount to set
	 * @return the new {@code Item} with the increased {@code amount}
	 */
	public Item increase(int amount) {
		return new Item(id, this.amount + amount);
	}

	/**
	 * Returns a new {@code Item} with the same information but with amount of the item set to the given
	 * argument.
	 * 
	 * @param amount
	 *            the amount to set
	 * @return the new {@code Item} with the specified {@code amount}
	 */
	public Item amount(int amount) {
		return new Item(id, amount);
	}

	/**
	 * Returns true if this {@code Item} can be stacked with the an {@code Item} with the same id.
	 * 
	 * @return true if it can stack with the same item
	 */
	public boolean isStackable() {
		return DataCenter.retrieveData("items", id, "stackable", Boolean.class);
	}

	public <T> T getData(String name, Class<T> cast) {
		return (T) DataCenter.retrieveData("items", id, name, cast);
	}
}
