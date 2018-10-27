package game.skill;

/**
 * A SkillHolder is one who holds the {@link game.skill.SkillSetManager} class for it's own use.
 * This allows for more flexibility with skill use so it may be available to almost anything in
 * RuneScape.
 * 
 * @author Albert Beaupre
 */
public interface SkillHolder {

	/**
	 * Returns the {@code SkillSetManager} of this {@code SkillHolder}.
	 * 
	 * @return the skill manager of this skill holder
	 */
	public SkillSetManager getSkills();

	/**
	 * Returns true if this {@code SkillHolder} has the given level requirement for the speicifed
	 * {@code SkillType}.
	 * 
	 * @param type
	 *            the type of level
	 * @param level
	 *            the level requirement
	 * @param modified
	 *            flag for any temporary level modifications allowed for this requirement check
	 * @return true if has level; return false otherwise
	 */
	public default boolean hasLevel(SkillType type, int level, boolean modified) {
		return getSkills().getLevel(type, modified) >= level;
	}

	/**
	 * @see game.skill.SkillSetManager
	 */
	public default void modifyLevel(SkillType type, int levels) {
		this.getSkills().modifyLevel(type, levels);
	}

	/**
	 * @see game.skill.SkillSetManager
	 */
	public default void modifyLevelByPercent(SkillType type, double percentage) {
		this.getSkills().modifyByPercent(type, percentage);
	}

	public void writeLevelPacket(SkillType type, int level);

}
