package util;

import java.util.HashMap;

import util.configuration.Configuration;

/**
 * The purpose of this class is to allow the accessibility to store and retrieve information for
 * things such as NPC, Items, or Shops.
 * 
 * @author Albert Beaupre
 */
public class DataCenter {

	private static final HashMap<String, Configuration> DATA = new HashMap<>();

	/**
	 * Retrieves the specific data relating to the {@code dataKey} and
	 * {@code sectionKey, sectionLocation} if they are existent and returns them casted as the specified
	 * {@code cast} class; null is returned otherwise.
	 * 
	 * @param dataKey
	 *            the key corresponding to the type of data to retrieve
	 * @param sectionKey
	 *            the key corresponding to the section
	 * @param sectionLocation
	 *            the key corresponding to the location of the section
	 * @return the data retrieved; return null otherwise
	 */
	@SuppressWarnings("unchecked")
	public static <T> T retrieveData(String dataKey, Object sectionKey, Object sectionLocation, Class<T> cast) {
		return (T) DATA.get(dataKey.toLowerCase()).getSection(sectionKey).get(sectionLocation);
	}

	/**
	 * Stores the given {@code configuration} to the data of this {@code DataCenter} class corresponding
	 * to the {@code dataKey} value.
	 * 
	 * @param dataKey
	 *            the key corresponding to the configuration
	 * @param configuration
	 *            the configuration to store
	 */
	public static void storeData(String dataKey, Configuration configuration) {
		DATA.put(dataKey, configuration);
	}

}
