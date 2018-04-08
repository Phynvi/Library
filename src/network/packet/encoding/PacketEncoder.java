package network.packet.encoding;

import network.ConnectionHolder;

/**
 * The {@code PacketEncoder} is used to create an {@code EncodedPacket} based on arguments that will
 * be sent to the client.
 * 
 * @author Albert Beaupre
 *
 * @param <C>
 *            The {@code ConnectionHolder} type
 */
public interface PacketEncoder<C extends ConnectionHolder> {

	/**
	 * Encodes a packet being sent based on the specified {@code args} argument and returns the
	 * {@code EncodedPacket}.
	 * 
	 * @param holder
	 *            the holder of the sent packet
	 * @param args
	 *            the arguments to use to encode the packet
	 * @return the {@code EncodedPacket}
	 */
	public EncodedPacket encode(C holder, Object... args);
}
