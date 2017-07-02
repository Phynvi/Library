package game.interfaces;

/**
 * Represents an interface usable within the game.
 * 
 * @author Albert Beaupre
 */
public abstract class Interface extends Window {

    private Window parent;

    /**
     * True if the interface may be clicked on, false if it is click through
     * (Example, some overlays like the wilderness overlay are actually
     * interfaces, and they ignore mouse clicks).
     */
    protected boolean clickable;

    /**
     * The position inside the parent this interface is to be placed in. This is
     * neither the parent nor the child interface ID, it is not an interface at
     * all. It has a unique ID for each position inside the parent.
     */
    protected short childPos;

    /**
     * Constructs a new {@code Interface}.
     * 
     * @param p
     *            the {@code PaneHolder} using this {@code Interface}
     * @param parent
     *            the parenting {@code Window} this {@code Interface} is placed
     *            on
     * @param childPos
     *            the position of this {@code Interface}
     * @param clickable
     *            the flag to check if this {@code Interface} can be clicked
     */
    public Interface(PaneHolder p, Window parent, int childPos, boolean clickable) {
	super(p);
	this.parent = parent;
	this.clickable = clickable;

	if (childPos > 0xFFFF)
	    throw new IllegalArgumentException("Bad childPos " + childPos);
	this.childPos = (short) childPos;
    }

    /**
     * When this interface has its 'X' pressed, should the client close the
     * interface immediately and notify the server (false) or should it notify
     * the server, in which case, the server will tell the client to close it
     * later.
     * 
     * @return true if closing is handled server-sided, false otherwise.
     */
    public abstract boolean isServerSidedClose();

    /**
     * True if this interface remains open when the player moves, false if it
     * should auto-close when the player attempts to move.
     * 
     * @return true if the interface can be used on the run.
     */
    public abstract boolean isMobile();

    /**
     * Opens this interface, but does not notify the player's windows.
     * Overriding this method would be useful if actions need to be taken when
     * this {@code Interface} is being opened.
     */
    public void onOpen() {}

    /**
     * Closes this interface, but does not notify the player's windows.
     * Overriding this method would be useful if actions need to be taken when
     * this {@code Interface} is being closed.
     */
    public void onClose() {}

    /**
     * Returns true if this {@code Interface} is open o within the
     * {@code PaneSet} of the {@code PaneHolder} of this {@code Interface}.
     * 
     * @return true if opened; return false otherwise
     */
    public final boolean isOpen() {
	return this.holder.getPanes().getActive().isOpen(this);
    }

    /**
     * Returns {@code true} if this {@code Interface} can be clicked; returns
     * {@code false} otherwise.
     * 
     * @return return true if clickable; return false otherwise
     */
    public boolean isClickable() {
	return clickable;
    }

    /**
     * Returns the {@code Window} this {@code Interface} is placed within.
     * 
     * @return the parenting window
     */
    public Window getParent() {
	return parent;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
	return getClass().getSimpleName();
    }
}