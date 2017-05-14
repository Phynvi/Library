package yaml;

/**
 * YMLSerializable should <b>NEVER</b> have a constructor. It is used through
 * reflection to create itself a new instance.
 * 
 * @author Albert Beaupre
 */
public interface YMLSerializable<E> {

    /**
     * Takes the specified {@code object} element and creates a serialized
     * {@code ConfigSection} based on it.
     * 
     * @param object
     *            the object to serialize
     * @return the serialized {@code ConfigSection} based on the specified
     *         object
     */
    public ConfigSection serialize(E object);

    /**
     * Deserializes the specified {@code ConfigSection} given based on the
     * element of this {@code YMLSerializable}.
     * 
     * @param section
     *            the {@code ConfigSection} to take for deserialization
     * @return the deserialized object
     */
    public E deserialize(ConfigSection section);

}
