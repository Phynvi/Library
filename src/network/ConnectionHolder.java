package network;

/**
 * A {@code ConnectionHolder} implementation is meant to hold a
 * {@code Connection} value by the {@link ConnectionHolder#getConnection()}
 * method.
 * 
 * @author Albert Beaupre
 */
public interface ConnectionHolder {

    /**
     * Returns the {@code Connection} value of this {@code ConnectionHolder}.
     * 
     * @return the connection
     */
    public Connection getConnection();

    /**
     * Returns the {@code Display} of the {@code Connection} within this
     * {@code ConnectionHolder}.
     * 
     * @return the display
     */
    public default Display getDisplay() {
	return getConnection().getDisplay();
    }

    /**
     * Writes the specified {@code object} upstream to the client.
     * 
     * @param object
     *            the object to write
     */
    public default void write(Object object) {
	getConnection().write(object);
    }

}
