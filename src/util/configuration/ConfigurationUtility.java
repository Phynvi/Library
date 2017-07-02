package util.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import util.yaml.FileConfig;

/**
 * 
 * @author Albert Beaupre
 */
public final class ConfigurationUtility {

    /**
     * Cannot create a new instance of this class.
     */
    private ConfigurationUtility() {

    }

    /**
     * Saves the specified {@code config} to the specified {@code file} based on
     * the {@link Configuration#getType()} value. If there is no value set, then
     * the configuration is saved default to {@link ConfigType#YAML}.
     * 
     * @param config
     *            the configuration to save
     * @param file
     *            the file to save to
     */
    public static final void saveToFile(Configuration config, File file) {
	if (file.isDirectory())
	    throw new IllegalArgumentException("A Configuration cannot be saved as a directory");

	switch (config.getType()) {
	    case BINARY:
		try {
		    FileOutputStream fos = new FileOutputStream(file);
		    ObjectOutputStream oos = new ObjectOutputStream(fos);
		    oos.writeObject(config.getConfigurables());
		    oos.close();
		    fos.close();
		    System.out.printf("Serialized HashMap data is saved in hashmap.ser");
		} catch (IOException ioe) {
		    ioe.printStackTrace();
		}
		break;
	    case JSON:
		break;
	    case TXT:
		break;
	    case YAML:
		FileConfig fc = new FileConfig(file);
		fc.save(config.getConfigurables());
		break;
	    default:
		break;
	}
    }

    /**
     * 
     * @param file
     * @return
     */
    public static Configuration readFromFile(File file) {
	return null;
    }
}
