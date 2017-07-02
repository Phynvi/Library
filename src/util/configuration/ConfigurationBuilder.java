package util.configuration;

import java.util.HashMap;

/**
 * 
 * @author Albert Beaupre
 */
public class ConfigurationBuilder {

    private HashMap<String, Object> configurables;
    protected ConfigType type;

    private ConfigurationBuilder() {

    }

    public static ConfigurationBuilder startBuilding() {
	ConfigurationBuilder builder = new ConfigurationBuilder();
	return builder;
    }

    public final ConfigurationBuilder type(ConfigType type) {
	this.type = type;
	return this;
    }

    public final ConfigurationBuilder addConfigurable(String name, Object value) {
	configurables.put(name, value);
	return this;
    }

    public Configuration build() {
	return new Configuration(type, configurables);
    }

}
