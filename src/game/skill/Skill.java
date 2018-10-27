package game.skill;

import util.configuration.ConfigSection;
import util.configuration.YMLSerializable;

/**
 * The Skill class is used to hold variables and methods relating to any in-game skills.
 * 
 * @author Albert Beaupre
 */
public class Skill implements YMLSerializable {

	private final SkillType type;

	private int currentLevel = 1;
	private int temporaryModification;
	private double experience;

	public Skill(SkillType type) {
		this.type = type;
	}

	/**
	 * Adds the given {@code experience} to this {@code Skill} and changes the current level based on
	 * the current experience.
	 * 
	 * @param experience
	 *            the experience to add
	 */
	public void addExperience(double experience) {
		this.experience += experience;
		this.currentLevel = SkillSetManager.getLevelForExperience(this.experience);
	}

	/**
	 * Modifies this {@code Skill} temporarily by the given {@code percentage} amount.
	 * 
	 * @param percentage
	 *            the percentage to temporarily level this skill
	 */
	public void modifyByPercent(double percentage) {
		this.temporaryModification = (int) (percentage * this.currentLevel);
	}

	/**
	 * Modifies this {@code Skill} temporarily by the given {@code levels} amount.
	 * 
	 * @param levels
	 *            the levels to temporarily add to this skill
	 */
	public void modifyLevel(int levels) {
		this.temporaryModification = levels;
	}

	/**
	 * Returns the current level of this {@code Skill}. If the given {@code modified} argument is true,
	 * then the temporary level is returned.
	 * 
	 * @param modified
	 *            the flag to return the temporary level
	 * @return the current level; return the temporary level if modified is true
	 */
	public int getLevel(boolean modified) {
		return modified ? currentLevel + temporaryModification : currentLevel;
	}

	public void setLevel(int level) {
		this.currentLevel = level;
		this.experience = SkillSetManager.getExperienceForLevel(level);
	}

	/**
	 * Returns the current amount of experience this {@code Skill} is at.
	 * 
	 * @return the experience this skill is at
	 */
	public double getExperience() {
		return experience;
	}

	@Override
	public ConfigSection serialize() {
		return new ConfigSection().set("level", currentLevel).set("mod", temporaryModification).set("exp", experience);
	}

	@Override
	public void deserialize(ConfigSection section) {
		this.currentLevel = section.getInt("level");
		this.temporaryModification = section.getInt("mod");
		this.experience = section.getDouble("exp");
	}

	public SkillType getType() {
		return type;
	}

}
