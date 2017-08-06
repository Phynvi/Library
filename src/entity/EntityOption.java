package entity;

/**
 * Represents a type of {@code EntityOption} used by an {@code Entity}.
 * 
 * @author Albert Beaupre
 */
public abstract class EntityOption {

    /**
     * This is the text displayed when the entityOption is viewed.
     */
    public final String text;

    /**
     * This flag is used to determine whether this {@code EntityOption} is placed
     * above the "Walk" entityOption.
     */
    public final boolean aboveWalk;

    /**
     * The index this {@code EntityOption} is placed at within an {@code Entity}.
     */
    public int index;

    /**
     * Constructs a new {@code EntityOption} with the given {@code text} to display
     * for the entityOption and the given {@code aboveWalk} flag to determine if this
     * {@code EntityOption} is placed above the "Walk" entityOption.
     * 
     * @param text
     * @param aboveWalk
     */
    public EntityOption(String text, boolean aboveWalk) {
	this.text = text;
	this.aboveWalk = aboveWalk;
    }

    /**
     * Constructs a new {@code EntityOption} with the given {@code text} to display
     * for the entityOption.
     * 
     * @param text
     *            the text to display for the entityOption
     */
    public EntityOption(String text) {
	this(text, false);
    }

    /**
     * This method is called when this {@code EntityOption} has been selected on the
     * specified {@code selected} argument by the given {@code interactor}.
     * 
     * @param interactor
     *            the {@code Entity} that has selected this {@code EntityOption}
     * @param selected
     *            the {@code Entity} that has been selected
     */
    protected abstract void select(Entity interactor, Entity selected);

}
