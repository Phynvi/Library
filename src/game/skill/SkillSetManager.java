package game.skill;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import infrastructure.Tick;
import util.configuration.ConfigSection;
import util.configuration.YMLSerializable;

/**
 * The SkillSetManager class is used to hold and manage a set of the
 * {@code Skill}s. Temporary skill level modification such as boosts or
 * decreases, leveling up, and anything else relating to a {@code Skill} is
 * handled within this class.
 * 
 * @author Albert Beaupre
 * 
 * @see game.skill.SkillType
 * @see game.skill.Skill
 */
public class SkillSetManager implements YMLSerializable {

	/**
	 * This is the maximum amount of experience a skill can be leveled to.
	 */
	public static final int MAX_EXP = 200000000;

	private final HashMap<SkillType, Skill> skills = new HashMap<>(SkillType.values().length);
	private final SkillHolder holder;
	private SkillTick skillTick;

	/**
	 * Constructs a new {@code SkillSetManager} with the given {@code SkillHolder}
	 * as the holder for this class.
	 * 
	 * @param holder
	 *            the holder of the class to use for itself
	 */
	public SkillSetManager(SkillHolder holder) {
		this.holder = holder;

		for (SkillType type : SkillType.values()) {
			Skill skill = new Skill();
			if (type == SkillType.HITPOINTS)
				skill.addExperience(getExperienceForLevel(10));
			skills.put(type, skill);
		}
	}

	/**
	 * Adds the given {@code experience} to the {@code Skill} with the specified
	 * {@code type} and calls the {@code SkillLevelUpEvent} if any level has
	 * increased in the skill.
	 * 
	 * @param type
	 *            the correlating skill type
	 * @param experience
	 *            the experience to add to the skill
	 */
	public void addExperience(SkillType type, double experience) {
		Skill skill = skills.get(type);
		int previousLevel = skill.getLevel(false);
		skill.addExperience(experience);
		int newLevel = skill.getLevel(false);

		if (previousLevel < newLevel) {
			SkillLevelUpEvent event = new SkillLevelUpEvent(holder, skill, previousLevel, newLevel);
			event.call();
		}
	}

	/**
	 * Modifies the level temporarily of the {@code Skill} with the specified
	 * {@code type} by the given {@code percentage} argument.
	 * 
	 * @param type
	 *            the type of the correlating skill
	 * @param percentage
	 *            the percentage to increase/decrease the skill level by
	 */
	public void modifyByPercent(SkillType type, double percentage) {
		Skill skill = skills.get(type);
		skill.modifyByPercent(percentage);

		if (skillTick == null)
			skillTick = new SkillTick();
		if (!this.skillTick.isQueued())
			this.skillTick.queue(60000);
	}

	/**
	 * Modifies the level temporarily of the {@code Skill} with the specified
	 * {@code type} by the given {@code levels} argument.
	 * 
	 * @param type
	 *            the type of the correlating skill
	 * @param levels
	 *            the amount of levels to increase/decrease the skill level by
	 */
	public void modifyLevel(SkillType type, int levels) {
		Skill skill = skills.get(type);
		skill.modifyLevel(levels);

		if (skillTick == null)
			skillTick = new SkillTick();
		if (!this.skillTick.isQueued())
			this.skillTick.queue(60000);
	}

	/**
	 * Returns the current level of the {@code Skill} with the specified
	 * {@code Type}. If the given {@code modified} argument is true, then the
	 * temporary level is returned.
	 * 
	 * @param type
	 *            the type of the correlating skill to get the level of
	 * @param modified
	 *            the flag to return the temporary level
	 * @return the current level; return the temporary level if modified is true
	 */
	public int getLevel(SkillType type, boolean modified) {
		return skills.get(type).getLevel(modified);
	}

	/**
	 * Returns the amount of experience the {@code Skill} with the specified
	 * {@code type} is at.
	 * 
	 * @param type
	 *            the type of the correlating skill to get the experience for
	 * @return the experience for the skill
	 */
	public double getExperience(SkillType type) {
		return skills.get(type).getExperience();
	}

	/**
	 * Returns the total level of all skills combined within the
	 * {@code SkillSetManager}.
	 * 
	 * @return the total level of all skills
	 */
	public int getTotalLevel() {
		int total = 0;
		for (Skill skill : skills.values())
			total += skill.getLevel(false);
		return total;
	}

	/**
	 * Returns the level when a skill is at the given {@code experience}.
	 * 
	 * @param experience
	 *            the experience to correlate to the level
	 * @return the experience needed for the given level
	 */
	public static int getLevelForExperience(double experience) {
		double points = 0, output = 0;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300 * Math.pow(2, lvl / 7.));
			output = Math.floor(points / 4);
			if (experience < output)
				return lvl;
		}
		return -1;
	}

	/**
	 * Returns the experience needed for the given {@code level} argument.
	 * 
	 * @param level
	 *            the level to return experience for
	 * @return the experience for the given level
	 */
	public static int getExperienceForLevel(int level) {
		double points = 0, output = 0;
		for (int lvl = 1; lvl < level; lvl++) {
			points += Math.floor(lvl + 300 * Math.pow(2, lvl / 7.));
			output = Math.floor(points / 4);
		}
		return (int) output;
	}

	/**
	 * Should never allow access to this class. These variables are only to be
	 * modified by this class.
	 */
	private class SkillTick extends Tick {

		@Override
		public void tick() {
			boolean anyModified = false;
			for (Skill skill : skills.values()) {
				int diff = skill.getLevel(true) - skill.getLevel(false);
				if (diff != 0) {
					skill.modifyLevel(diff > 0 ? diff - 1 : diff + 1);
					anyModified = true;
				}
			}
			if (anyModified)
				queue(60000);
		}

	}

	@Override
	public ConfigSection serialize() {
		ConfigSection config = new ConfigSection();
		for (Entry<SkillType, Skill> entry : skills.entrySet())
			config.put(entry.getKey().name(), entry.getValue().serialize());
		return config;
	}

	@Override
	public void deserialize(ConfigSection section) {
		for (Entry<String, Object> entry : section.entrySet()) {
			@SuppressWarnings("unchecked") Map<String, Object> map = (Map<String, Object>) entry.getValue();

			SkillType type = SkillType.valueOf(entry.getKey());
			skills.get(type).deserialize(new ConfigSection(map));
		}
	}

}
