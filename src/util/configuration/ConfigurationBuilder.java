package util.configuration;

import java.lang.reflect.Field;

/**
 * 
 * @author Albert Beaupre
 */
public final class ConfigurationBuilder {

	private ConfigSection configurables;
	protected ConfigType type;

	private ConfigSection currentSection;

	/**
	 * Constructs a new {@code ConfigurationBuilder}, which is not inheritable and cannot be created as
	 * a new instance.
	 */
	private ConfigurationBuilder() {
		this.configurables = new ConfigSection();
	}

	/**
	 * Instantiates a new {@code ConfigurationBuilder} so you may start constructing a
	 * {@code Configuration}.
	 * 
	 * @return a new {@code ConfigurationBuilder}.
	 */
	public static ConfigurationBuilder startBuilding() {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		return builder;
	}

	/**
	 * Sets the {@code ConfigType} of the {@code Configuration} to be built.
	 * 
	 * @param type
	 *            {@code ConfigType}
	 * @return this current {@code ConfigurationBuilder} instance for chaining
	 */
	public ConfigurationBuilder type(ConfigType type) {
		this.type = type;
		return this;
	}

	/**
	 * Starts a new section to build on with data for the {@code Configuration}.
	 * 
	 * @param key
	 *            the key of the new section
	 * @return this current {@code ConfigurationBuilder} instance for chaining
	 */
	public ConfigurationBuilder section(Object key) {
		configurables.put("" + key, this.currentSection = new ConfigSection());
		return this;
	}

	/**
	 * Takes the fields within the specified {@code object} (if accessible) that are annotated with the
	 * {@link util.configuration.Configurable} class, and stores them as a single section with the
	 * fields as the data within the section. The specified {@code sectionKey} is the corresponding key
	 * to the section.
	 * 
	 * @param sectionKey
	 *            the corresponding key to the section
	 * @param object
	 *            the object to configure
	 * @return this current {@code ConfigurationBuilder} instance for chaining
	 */
	public ConfigurationBuilder configureObjectToSection(Object sectionKey, Object object) {
		section(sectionKey);
		for (Field field : object.getClass().getFields()) {
			if (field.isAnnotationPresent(Configurable.class)) {
				try {
					add(field.getName(), field.get(object));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return this;
	}

	/**
	 * Adds the specified {@code value} with a corresponding {@code name} to the current section being
	 * built on the {@code Configuration}.
	 * 
	 * @param name
	 *            the corresponding name of the value
	 * @param value
	 *            the value to add
	 * @return this current {@code ConfigurationBuilder} instance for chaining
	 */
	public ConfigurationBuilder add(String name, Object value) {
		if (currentSection == null)
			throw new NullPointerException("You must specify the current section to add to");
		currentSection.put(name, value);
		return this;
	}

	/**
	 * Constructs a new {@code Configuration} based on the values built within this
	 * {@code ConfigurationBuilder}.
	 * 
	 * @return the newly constructed {@code Configuration}
	 */
	public Configuration build() {
		return new Configuration(type, configurables);
	}
}
