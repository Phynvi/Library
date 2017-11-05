package container;

/**
 * Represents an item with a specific id used within a {@link container.Container}. A {@code Item}
 * can have a modifiable amount, but not a modifiable id.
 * 
 * @author Albert Beaupre
 */
public class Item {

	private final short id; // This value should not be modifiable
	private int amount;

	/**
	 * Constructs a new {@code Item} from the specified arguments.
	 * 
	 * @param id
	 *           the id of the item
	 * @param amount
	 *           the amount of the item
	 */
	public Item(int id, int amount) {
		this.id = (short) id;
		this.amount = amount;
	}

	/**
	 * Constructs a new {@code Item} from the specified arguments.
	 * 
	 * @param id
	 *           the id of the item
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
	 * Returns a new {@code Item} with the same information but with amount of the item decreased by
	 * the given argument.
	 * 
	 * @param amount
	 *           the amount to set
	 * @return the new {@code Item} with the decreased {@code amount}
	 */
	public Item decrease(int amount) {
		return new Item(id, this.amount - amount);
	}

	/**
	 * Returns a new {@code Item} with the same information but with amount of the item increased by
	 * the given argument.
	 * 
	 * @param amount
	 *           the amount to set
	 * @return the new {@code Item} with the increased {@code amount}
	 */
	public Item increase(int amount) {
		return new Item(id, this.amount + amount);
	}

	/**
	 * Returns a new {@code Item} with the same information but with amount of the item set to the
	 * given argument.
	 * 
	 * @param amount
	 *           the amount to set
	 * @return the new {@code Item} with the specified {@code amount}
	 */
	public Item amount(int amount) {
		return new Item(id, amount);
	}

	public boolean isStackable() {
		return false;
	}
}
