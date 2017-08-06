package util.configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

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
     * @throws UnsupportedOperationException
     *             if the file is a directory
     */
    public static final void saveToFile(Configuration config, File file) {
	if (file.isDirectory())
	    throw new UnsupportedOperationException("A Configuration cannot be saved as a directory");

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
	    case TXT: //The txt format will just be saved to YAML because YAML is easily readable (for now)
	    case YAML:
		FileConfig fc = new FileConfig(file);
		fc.save(config.getConfigurables());
		break;
	    default:
		break;
	}
    }

    /**
     * Saves the specified {@code object} to the given {@code file}. If there is
     * any {@link java.lang.reflect.Field} with the annotation
     * {@link util.configuration.Configurable} above it, then it will be saved
     * in the format of the specified {@code type}.
     * 
     * @param object
     *            the object to save
     * @param type
     *            the config type to save the file as
     * @param file
     *            the file to save to
     */
    public static final void saveToFile(Object object, ConfigType type, File file) {
	HashMap<String, Object> configurables = new HashMap<>();

	for (Field field : object.getClass().getFields()) {
	    if (field.isAnnotationPresent(Configurable.class)) {
		try {
		    configurables.put(field.getName(), field.get(object));
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
	Configuration configuration = new Configuration(type, configurables);
	saveToFile(configuration, file);
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
