package entity.actor.persona;

import java.util.HashMap;
import java.util.Map.Entry;

import util.yaml.ConfigSection;
import util.yaml.YMLSerializable;
import container.BasicItem;
import container.Container;
import container.Containers;
import entity.Entity;
import entity.actor.ActionQueue;
import entity.actor.Actor;
import entity.geometry.Location;

/**
 * 
 * @author Albert Beaupre
 */
public abstract class Persona extends Entity implements Actor, YMLSerializable {

    /**
     * The {@code attachments} map is used to attach any {@code YMLSerializable}
     * values to this {@code Persona}.
     */
    protected HashMap<String, YMLSerializable> attachments = new HashMap<>();

    /**
     * The {@code config} variable is used to store any configurations to this
     * {@code Persona} that can be serialized.
     */
    protected ConfigSection config;

    private final ActionQueue<Persona> actions = new ActionQueue<>();

    private Container<BasicItem> inventory, equipment;
    private final PersonaOption[] options;

    /**
     * Constructs a new {@code Persona}.
     */
    public Persona(Location location) {
	super(location);
	this.inventory = new Container<>(Containers.AVAILABLE_STACK, 28, 1, Integer.MAX_VALUE);
	this.config = new ConfigSection();
	this.options = new PersonaOption[5];
    }

    /**
     * This registers the given serializable object with this Persona. If the
     * config has already been loaded, then the deserialize() method is called
     * on the given object, using the ConfigSection available in this Persona's
     * config at the given key. If the config has not yet been loaded (This is
     * done in the load(File f) call), then the serializable object is still
     * registered under the given key, and then when the config is loaded, the
     * deserialize() method is called with the appropriate ConfigSection as an
     * argument.
     * 
     * Calling this method also means that when this Persona has serialize()
     * called, it will call the serialize() method on the given YML object and
     * set the ConfigSection at the given key to the result. The result of this
     * serialization is then written to disk, allowing persistant data to be
     * stored across Personas.
     * 
     * @param key
     *            the key for the config section.
     * @param yml
     *            the object to deserialize/serialize.
     * @return true if the object was deserialized, false if the server is still
     *         waiting for the player to load.
     */
    public boolean register(String key, YMLSerializable yml) {
	if (key == null)
	    throw new NullPointerException("Key may not be null.");
	if (yml == null)
	    throw new NullPointerException("YMLSerializable object may not be null.");

	this.attachments.put(key, yml);

	if (this.config != null) {
	    yml.deserialize(this.config.getSection(key));
	    return true;
	}
	return false;
    }

    public ConfigSection getConfig() {
	return config;
    }

    /*
     * (non-Javadoc)
     * 
     * @see entity.Interactable#options()
     */
    public PersonaOption[] options() {
	return options;
    }

    /*
     * (non-Javadoc)
     * 
     * @see entity.Interactable#getName()
     */
    public String getName() {
	return this.config.getString("name");
    }

    public Container<BasicItem> getEquipment() {
	return equipment;
    }

    /*
     * (non-Javadoc)
     * 
     * @see entity.actor.Actor#getContainer()
     */
    public Container<? extends BasicItem> getInventory() {
	return inventory;
    }

    /*
     * (non-Javadoc)
     * 
     * @see entity.actor.Actor#getActions()
     */
    public ActionQueue<Persona> getActions() {
	return actions;
    }

    /*
     * (non-Javadoc)
     * 
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
     * 
     * @see util.yaml.YMLSerializable#deserialize(util.yaml.ConfigSection)
     */
    public void deserialize(ConfigSection section) {
	this.config = section;
	for (Entry<String, YMLSerializable> e : this.attachments.entrySet())
	    e.getValue().deserialize(section.getSection(e.getKey()));
    }

}
