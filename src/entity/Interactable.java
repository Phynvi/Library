package entity;

/**
 * The {@code Interactable} implementation is used for any type of
 * {@code Entity} that is specified as 'interactable', meaning they can be
 * interacted with options.
 * 
 * @author Albert Beaupre
 */
public interface Interactable {

    /**
     * Returns an array of the {@code Option} data of this {@code Interactable}.
     * 
     * @return the options
     */
    public Option[] options();

    /**
     * 
     * @param option
     */
    public default void setOption(Option option) {
	options()[option.getIndex()] = option;
    }

    /**
     * Returns the name of this {@code Interactable}.
     * 
     * @return the name
     */
    public String getName();

}
