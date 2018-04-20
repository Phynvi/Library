package util.loot;

/**
 * Represents an item used to be selected for loot.
 * 
 * @author Albert Beaupre
 */
public interface LootableItem {

	/**
	 * Returns the chance rate at which this {@code LootableItem} should be selected.
	 * 
	 * @return the chance rate
	 */
	public double getChance();

}
