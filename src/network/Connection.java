package network;

import java.util.Objects;

import io.netty.channel.Channel;

/**
 * A {@code Connection} represents a connection through a channel relating to a client and server.
 * 
 * @author Albert Beaupre
 */
public class Connection {

	private final Channel channel;
	private final int revision;
	private Display display;

	/**
	 * Constructs a new {@code Connection} based on the specified {@code channel} that is associated
	 * with the specified {@code revision}.
	 * 
	 * @param channel
	 *            the channel of this {@code Connection}.
	 * 
	 * @param revision
	 *            the revision of the server this connection is associated with
	 */
	public Connection(Channel channel, int revision) {
		this.channel = channel;
		this.revision = revision;
	}

	/**
	 * Writes the specified {@code object} so it may be sent to the client.
	 * 
	 * @throws NullPointerException
	 *             if the object is null
	 * 
	 * @param object
	 *            the object to write through this connection
	 */
	public void write(Object object) {
		if (object == null)
			throw new NullPointerException("You cannot write a NULL object through a Connection");
		channel.writeAndFlush(object);
	}

	/**
	 * Returns the {@code Channel} through this {@code Connection}.
	 * 
	 * @return the channel
	 */
	public Channel getChannel() {
		return channel;
	}

	/**
	 * Returns the {@code Display} for this {@code Connection}.
	 * 
	 * @return the display
	 */
	public Display getDisplay() {
		return display;
	}

	/**
	 * Sets the {@code Display} of this {@code Connection} to the specified argument.
	 * 
	 * @param display
	 *            the display to set
	 */
	public void setDisplay(Display display) {
		this.display = display;
	}

	/**
	 * Returns the server revision this {@code Connection} is associated with.
	 * 
	 * @return the revision
	 */
	public int getRevision() {
		return revision;
	}

	@Override
	public int hashCode() {
		return Objects.hash(revision, display, channel);
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Connection))
			return false;
		Connection con = (Connection) o;
		return con.revision == revision && display.equals(con.display) && channel.equals(con.channel);
	}

}
