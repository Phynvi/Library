package yaml;

import java.io.File;
import java.io.FileInputStream;

/**
 * This class contains useful methods for any sort of YML implementations.
 * 
 * @author Albert Beaupre
 *
 * @see yaml.YMLSerializable
 * @see ConfigSection
 */
public class YMLUtility {

    /**
     * Loads yaml information from the given {@code file} and returns the
     * {@code YMLSerializable} object based on the specified
     * {@code serializableClass}.
     * 
     * @param file
     *            the file to load yaml from
     * @param serializableClass
     *            the class type to create a {@code YMLSerializable} object from
     * @return the {@code YMLSerializable} object
     */
    public static <E> E loadYMLSerializable(File file, Class<? extends YMLSerializable<E>> serializableClass) {
	try {
	    return serializableClass.newInstance().deserialize(new ConfigSection(new FileInputStream(file)));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * Loads yaml information from the given {@code yaml} script and returns the
     * {@code YMLSerializable} object based on the specified
     * {@code serializableClass}.
     * 
     * @param yaml
     *            the yaml script to read
     * @param serializableClass
     *            the class type to create a {@code YMLSerializable} object from
     * @return the {@code YMLSerializable} object
     */
    public static <E> E loadYMLSerializable(String yaml, Class<? extends YMLSerializable<E>> serializableClass) {
	try {
	    return serializableClass.newInstance().deserialize(new ConfigSection(yaml));
	} catch (Exception e) {
	    e.printStackTrace();
	}
	return null;
    }
}
