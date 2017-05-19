package network;

import io.netty.channel.Channel;

/**
 * A {@code Connection} represents a connection through a channel relating to a
 * client and server.
 * 
 * @author Albert Beaupre
 */
public class Connection {

    private final Channel channel;

    /**
     * Constructs a new {@code Connection} based on the specified
     * {@code channel}.
     * 
     * @param channel
     *            the channel of this {@code Connection}.
     */
    public Connection(Channel channel) {
	this.channel = channel;
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

}
