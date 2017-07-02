package container;

/**
 * Represents an item with a specific id used within a
 * {@link container.Container}. A {@code Item} can have a modifiable amount, but
 * not a modifiable id.
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
     * Sets the amount of this {@code Item} to the specified value.
     * 
     * @param amount
     *            the amount to set
     * @return the current {@code Item} with the specific {@code amount}
     */
    public Item amount(int amount) {
	this.amount = amount;
	return this;
    }

    public boolean isStackable() {
	return false;
    }
}
