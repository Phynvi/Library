package game.skill;

/**
 * A SkillHolder is one who holds the {@link game.skill.SkillSetManager} class
 * for it's own use. This allows for more flexibility with skill use so it may
 * be available to almost anything in RuneScape.
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

}
