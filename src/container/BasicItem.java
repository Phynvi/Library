package container;

/**
 * Represents an item with a specific id used within a {@link Container}. A
 * {@code BasicItem} can have a modifiable amount, but not a modifiable id.
 * 
 * @author Albert Beaupre
 */
public class BasicItem {

    private final short id; // This value should not be modifiable
    private int amount;

    /**
     * Constructs a new {@code BasicItem} from the specified arguments.
     * 
     * @param id
     *            the id of the item
     * @param amount
     *            the amount of the item
     */
    public BasicItem(int id, int amount) {
	this.id = (short) id;
	this.amount = amount;
    }

    /**
     * Constructs a new {@code BasicItem} from the specified arguments.
     * 
     * @param id
     *            the id of the item
     */
    public BasicItem(int id) {
	this(id, 1);
    }

    /**
     * Returns the id of this {@code BasicItem}.
     * 
     * @return the id
     */
    public int getId() {
	return id;
    }

    /**
     * Returns the amount of this {@code BasicItem}.
     * 
     * @return the amount
     */
    public int getAmount() {
	return amount;
    }

    /**
     * Sets the amount of this {@code BasicItem} to the specified value.
     * 
     * @param amount
     *            the amount to set
     * @return the current {@code BasicItem} with the specific {@code amount}
     */
    public BasicItem amount(int amount) {
	this.amount = amount;
	return this;
    }

    public boolean isStackable() {
	return false;
    }

}
