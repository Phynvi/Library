package network.packet;

/**
 * Represents the type of {@code Packet}, which is used for different types of packet encoding.
 * 
 * @author Albert Beaupre
 */
public enum PacketType {

	/**
	 * A standard {@code PacketType}, which doesn't have its length written.
	 */
	STANDARD(0),

	/**
	 * This type of {@code Packet} has its length written as a byte when being sent to the client.
	 */
	VAR_BYTE(1),

	/**
	 * This type of {@code Packet} has its length written as a short when being sent to the client.
	 */
	VAR_SHORT(2);

	private final int size;

	/**
	 * Constructs a new {@code PacketType} from the specified {@code size}.
	 * 
	 * @param size
	 *            the size of this message type
	 */
	private PacketType(int size) {
		this.size = size;
	}

	/**
	 * Returns the size of this {@code PacketType}.
	 * 
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

}
