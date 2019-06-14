package game.skill;

/**
 * Skill ids are determined by their location in this {@code Skill} enum and their names are
 * determined by their enum name.
 * 
 * @author Albert Beaupre
 */
public enum SkillType {
	
	ATTACK(0),
	DEFENCE(1),
	STRENGTH(2),
	HITPOINTS(3),
	RANGED(4),
	PRAYER(5),
	MAGIC(6),
	COOKING(7),
	WOODCUTTING(8),
	FLETCHING(9),
	FISHING(10),
	FIREMAKING(11),
	CRAFTING(12),
	SMITHING(13),
	MINING(14),
	HERBLORE(15),
	AGILITY(16),
	THIEVING(17),
	SLAYER(18),
	FARMING(19),
	RUNECRAFTING(20),
	HUNTER(21),
	CONSTRUCTION(22),
	SUMMONING(23),
	DUNGEONEERING(24);

	private final int id;

	/**
	 * The purpose for having a parameter instead of using ordinal is in the case of any accidental code
	 * change or reorganization that isn't correlating to what is essential.
	 * 
	 * @param id
	 *            the id of the skill
	 */
	private SkillType(int id) {
		this.id = id;
	}

	/**
	 * Returns the id of this {@code Skills}.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the name of this {@code Skill} with lower case letters and a capitalized first letter.
	 * 
	 * @return the name of this {@code Skill} with a capitalized first letter.
	 */
	public String toString() {
		String n = super.name();
		return n.substring(0, 1) + n.substring(1).toLowerCase();
	}

}
