package cache.data;

import java.io.File;
import util.configuration.ConfigSection;
import util.configuration.ConfigType;
import util.configuration.Configuration;
import util.configuration.FileConfig;

/**
 * 
 * @author Albert Beaupre
 */
public class ItemData extends Configuration {

	private ItemData(ConfigType type, ConfigSection sections) {
		super(type, sections);
	}

	/**
	 * 
	 * @param itemId
	 * @return
	 */
	public boolean isStackable(int itemId) {
		return this.retrieveData(itemId, "stackable", Boolean.class);
	}

	/**
	 * 
	 * @param file
	 * @return
	 */
	public static final ItemData load(File file, ConfigType type) {
		FileConfig config = new FileConfig(file);
		config.load(type);
		return new ItemData(type, config);
	}

}
