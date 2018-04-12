package game.skill;

/**
 * Skill ids are determined by their location in this {@code Skill} enum and
 * their names are determined by their enum name.
 * 
 * @author Albert Beaupre
 */
public enum SkillType {
	ATTACK,
	DEFENCE,
	STRENGTH,
	HITPOINTS,
	RANGED,
	PRAYER,
	MAGIC,
	COOKING,
	WOODCUTTING,
	FLETCHING,
	FISHING,
	FIREMAKING,
	CRAFTING,
	SMITHING,
	MINING,
	HERBLORE,
	AGILITY,
	THIEVING,
	SLAYER,
	FARMING,
	RUNECRAFTING,
	HUNTER,
	CONSTRUCTION,
	DUNGEONEERING;

	private SkillType() {

	}

	/**
	 * Returns the id of this {@code Skills}.
	 * 
	 * @return the id
	 */
	public int getId() {
		return this.ordinal();
	}

	/**
	 * Returns the name of this {@code Skill} with lower case letters and a
	 * capitalized first letter.
	 * 
	 * @return the name of this {@code Skill} with a capitalized first letter.
	 */
	public String toString() {
		String n = super.name();
		return n.substring(0, 1) + n.substring(1).toLowerCase();
	}

}
