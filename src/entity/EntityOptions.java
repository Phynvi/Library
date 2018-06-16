package entity;

/**
 * 
 * @author Albert Beaupre
 */
public class EntityOptions {

	/**
	 * The maximum number of entityOptions that can exist
	 */
	public static final byte MAX_OPTIONS = 8;

	private EntityOption[] entityOptions = new EntityOption[MAX_OPTIONS];
	private final Entity entity;

	/**
	 * 
	 * @param entity
	 */
	public EntityOptions(Entity entity) {
		this.entity = entity;
	}

	/**
	 * 
	 * @param index
	 * @param target
	 */
	public void select(int index, Entity target) {
		if (index >= entityOptions.length || entityOptions[index] == null)
			throw new NullPointerException("That entityOption does not exist on target entity: " + index);
		if (target == null)
			throw new NullPointerException("The Entity target selected for the entityOption cannot be null");

		EntitySelectOptionEvent event = new EntitySelectOptionEvent(entity, target, entityOptions[index]);
		event.call();
		if (event.isCancelled())
			return;

		entityOptions[index].select(entity, target);
	}

	/**
	 * 
	 * @param text
	 * @param target
	 */
	public void select(String text, Entity target) {
		for (int i = 0; i < entityOptions.length; i++) {
			if (entityOptions[i].text.equalsIgnoreCase(text)) {
				select(i, target);
				return;
			}
		}
	}

	/**
	 * 
	 * @param entityOption
	 */
	public void add(EntityOption entityOption) {
		if (entityOption == null)
			return;
		for (int i = 0; i < entityOptions.length; i++) {
			if (entityOptions[i] == entityOption)
				return;
			if (entityOptions[i] == null) {
				set(i, entityOption);
				return;
			}
		}
	}

	/**
	 * 
	 * @param entityOption
	 */
	public void remove(EntityOption entityOption) {
		for (int i = 0; i < entityOptions.length; i++) {
			if (entityOptions[i] == entityOption) {
				set(i, null);
				break; // There really should only be one occurrence, I don't see
						// how another can exist
			}
		}
	}

	/**
	 * 
	 * @param name
	 */
	public void remove(String name) {
		for (int i = 0; i < entityOptions.length; i++) {
			if (entityOptions[i] != null && entityOptions[i].text.equalsIgnoreCase(name)) {
				set(i, null);
				return;
			}
		}
	}

	/**
	 * 
	 * @param index
	 */
	public void remove(int index) {
		set(index, null);
	}

	/**
	 * 
	 * @param index
	 * @param entityOption
	 */
	public void set(int index, EntityOption entityOption) {
		int previousIndex = entityOption.index;
		entityOption.index = index;
		EntityChangeOptionEvent event = new EntityChangeOptionEvent(entity, entityOptions[index], entityOption);
		event.call();
		if (event.isCancelled()) {
			entityOption.index = previousIndex;
			return;
		}

		this.entityOptions[index] = entityOption;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuilder builder = new StringBuilder("EntityOptions {\n");
		for (int i = 0; i < entityOptions.length; i++) {
			if (entityOptions[i] != null)
				builder.append(String.format("\toption[index=%s, text=%s, aboveWalk=%s],\n", i, entityOptions[i].text, entityOptions[i].aboveWalk));
		}
		return builder.append("}").toString();
	}

}
