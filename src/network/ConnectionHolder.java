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

}
