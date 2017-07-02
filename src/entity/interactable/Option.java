package entity.interactable;

/**
 * Represents a type of {@code Option} used by an {@code Interactable}.
 * 
 * @author Albert Beaupre
 */
public interface Option {

    /**
     * This method is called when this {@code Option} has been selected on the
     * specified {@code selected} argument by the specified {@code interator}.
     * 
     * @param interactor
     *            the {@code Interactable} that has selected this {@code Option}
     * @param selected
     *            the {@code Interactable} that has been selected
     */
    public void select(Interactable interactor, Interactable selected);

    /**
     * Returns the name of this {@code Option}.
     * 
     * @return the name
     */
    public String getOptionName();

    /**
     * Returns the index of this {@code Option}.
     * 
     * @return the index
     */
    public int getIndex();

}
