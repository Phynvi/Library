package util.configuration;

import java.util.HashMap;

/**
 * The {@code Configuration} class is used to create configurable objects that
 * can be written in a specific format, which is based on the
 * {@link Configuration#getType()} method.
 * 
 * @author Albert Beaupre
 */
public class Configuration {

    private final ConfigType type;
    private final HashMap<String, Object> configurables;

    /**
     * Constructs a new {@code Configuration} with the specified
     * {@code ConfigType} to use as writing the {@code configurables} objects
     * as.
     * 
     * @param type
     *            the type of configuration to write
     * @param configurables
     *            the objects that are configurable
     */
    public Configuration(ConfigType type, HashMap<String, Object> configurables) {
	this.type = type;
	if (configurables != null) {
	    this.configurables = new HashMap<>(configurables);
	} else {
	    this.configurables = new HashMap<>();
	}
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
    public HashMap<String, Object> getConfigurables() {
	return new HashMap<>(configurables);
    }
}
