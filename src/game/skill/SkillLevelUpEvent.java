package game.skill;

import event.Event;

public class SkillLevelUpEvent extends Event {

	private final SkillHolder holder;
	private final Skill skill;
	private final int fromLevel;
	private final int toLevel;

	public SkillLevelUpEvent(SkillHolder holder, Skill skill, int fromLevel, int toLevel) {
		this.holder = holder;
		this.skill = skill;
		this.fromLevel = fromLevel;
		this.toLevel = toLevel;
	}

	public SkillHolder getSkillHolder() {
		return holder;
	}

	public Skill getSkill() {
		return skill;
	}

	public int getFromLevel() {
		return fromLevel;
	}

	public int getToLevel() {
		return toLevel;
	}

}
