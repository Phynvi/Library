package util.configuration;

import java.util.HashMap;

/**
 * The {@code Configuration} class is used to create configurable objects that can be written in a
 * specific format, which is based on the {@link Configuration#getType()} method.
 * 
 * @author Albert Beaupre
 */
public class Configuration {

	private final ConfigType type;
	private final HashMap<String, ConfigSection> sections;

	/**
	 * Constructs a new {@code Configuration} with the specified {@code ConfigType} to use as writing
	 * the {@code sections} objects as.
	 * 
	 * @param type
	 *           the type of configuration to write
	 * @param sections
	 *           the objects that are configurable
	 */
	public Configuration(ConfigType type, HashMap<String, ConfigSection> sections) {
		this.type = type;
		if (sections != null) {
			this.sections = new HashMap<>(sections);
		} else {
			this.sections = new HashMap<>();
		}
	}

	/**
	 * Returns the {@code ConfigSection} with the corresponding {@code key}, is existing; otherwise
	 * return null;
	 * 
	 * @param key
	 *           the key associated with the section
	 * @return the {@code ConfigSection} associated with the {@code key}; return null otherwise
	 */
	public ConfigSection getSection(String key) {
		return sections.get(key);
	}

	/**
	 * Retrieves the specific data relating to the {@code sectionKey} and {@code sectionLocation} if
	 * they are existent and returns them casted as the specified {@code cast} class; null is
	 * returned otherwise.
	 * 
	 * @param sectionKey
	 *           the key corresponding to the section
	 * @param sectionLocation
	 *           the key corresponding to the location of the section
	 * @return the data retrieved; return null otherwise
	 */
	@SuppressWarnings("unchecked")
	public <T> T retrieveData(String sectionKey, String sectionLocation, Class<T> cast) {
		return (T) getSection(sectionKey).get(sectionLocation);
	}

	/**
	 * Returns the {@code ConfigType} of this {@code Configuration}.
	 * 
	 * @return the type
	 */
	public ConfigType getType() {
		return type;
	}

	/**
	 * Returns a <b>new</b> {@code Map} of the configurable objects within this
	 * {@code Configuration}.
	 * 
	 * @return the new map of configurable objects
	 */
	public HashMap<String, ConfigSection> getConfigurables() {
		return new HashMap<>(sections);
	}
}
