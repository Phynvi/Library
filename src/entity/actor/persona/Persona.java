package entity.actor.persona;

import java.util.HashMap;
import java.util.Map.Entry;
import container.Container;
import container.Containers;
import container.Item;
import entity.Entity;
import entity.actor.ActionQueue;
import entity.actor.Actor;
import entity.geometry.Location;
import util.configuration.ConfigSection;
import util.configuration.YMLSerializable;

/**
 * 
 * @author Albert Beaupre
 */
public abstract class Persona extends Entity implements Actor, YMLSerializable {

	private final ActionQueue<Persona> actions = new ActionQueue<>();

	/**
	 * The {@code attachments} map is used to attach any {@code YMLSerializable} values to this
	 * {@code Persona}.
	 */
	private HashMap<String, YMLSerializable> attachments = new HashMap<>();

	/**
	 * The {@code config} variable is used to store any configurations to this {@code Persona} that
	 * can be serialized.
	 */
	public ConfigSection config = new ConfigSection();

	/**
	 * This method must be set to true if this {@code Persona} is active within the game; otherwise
	 * set it to false.
	 */
	public boolean active;

	public final Container<Item> inventory = new Container<>(Containers.AVAILABLE_STACK, 28, 1, Integer.MAX_VALUE);
	public final Container<Item> equipment = new Container<>(Containers.AVAILABLE_STACK, 14, 1, Integer.MAX_VALUE);

	/**
	 * Constructs a new {@code Persona} to be created.
	 */
	public Persona() {
	}

	/**
	 * This registers the given serializable object with this Persona. If the config has already been
	 * loaded, then the deserialize() method is called on the given object, using the ConfigSection
	 * available in this Persona's config at the given key. If the config has not yet been loaded
	 * (This is done in the load(File f) call), then the serializable object is still registered
	 * under the given key, and then when the config is loaded, the deserialize() method is called
	 * with the appropriate ConfigSection as an argument.
	 * 
	 * Calling this method also means that when this Persona has serialize() called, it will call the
	 * serialize() method on the given YML object and set the ConfigSection at the given key to the
	 * result. The result of this serialization is then written to disk, allowing persistant data to
	 * be stored across Personas.
	 * 
	 * @param key
	 *           the key for the config section.
	 * @param yml
	 *           the object to deserialize/serialize.
	 * @return true if the object was deserialized, false if the server is still waiting for the
	 *         holder to load.
	 */
	public boolean register(String key, YMLSerializable yml) {
		if (key == null)
			throw new NullPointerException("Key may not be null.");
		if (yml == null)
			throw new NullPointerException("YMLSerializable object may not be null.");

		this.attachments.put(key, yml);

		if (this.config != null) {
			yml.deserialize(this.config.getUnderlyingSection(key));
			return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see entity.Interactable#getName()
	 */
	public String getName() {
		return this.config.getString("name", "N/A");
	}

	/*
	 * (non-Javadoc)
	 * @see entity.actor.Actor#getActions()
	 */
	public ActionQueue<Persona> getActions() {
		return actions;
	}

	/*
	 * (non-Javadoc)
	 * @see util.yaml.YMLSerializable#serialize()
	 */
	public ConfigSection serialize() {
		Location loc = getLocation();
		if (loc != null)
			this.config.set("location", loc.serialize());
		for (Entry<String, YMLSerializable> e : this.attachments.entrySet())
			this.config.set(e.getKey(), e.getValue().serialize());
		return this.config;
	}

	/*
	 * (non-Javadoc)
	 * @see util.yaml.YMLSerializable#deserialize(util.yaml.ConfigSection)
	 */
	public void deserialize(ConfigSection section) {
		this.config = section;
		for (Entry<String, YMLSerializable> e : this.attachments.entrySet())
			e.getValue().deserialize(section.getUnderlyingSection(e.getKey()));
	}

}
