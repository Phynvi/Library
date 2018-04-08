package network.packet.decoding;

import io.netty.buffer.ByteBuf;
import network.packet.Packet;
import network.packet.PacketType;

/**
 * The {@code DecodedPacket} is a type of {@code Packet} that has decoded bytes sent from the client
 * and turns those bytes into readable information.
 * 
 * @author Albert Beaupre
 */
public class DecodedPacket extends Packet {

	/**
	 * Constructs a new {@code DecodedPacket} based on the specified arguments.
	 * 
	 * @param type
	 *            the {@code PacketType} of this {@code DecodedPacket}
	 * @param bytes
	 *            the information in bytes of this {@code DecodedPacket}
	 * @param opcode
	 *            the opcode of this {@code DecodedPacket}
	 */
	public DecodedPacket(PacketType type, ByteBuf bytes, int opcode) {
		super(type, bytes, opcode);
	}

	public int readLEInt() {
		return readUnsignedByte() + (readUnsignedByte() << 8) + (readUnsignedByte() << 16) + (readUnsignedByte() << 24);
	}

	public byte readByte() {
		return bytes.readByte();
	}

	public int readUnsignedByte() {
		return bytes.readUnsignedByte();
	}

	public byte readByteA() {
		return (byte) (readByte() - 128);
	}

	public byte readByteS() {
		return (byte) (128 - readByte());
	}

	public byte readByteC() {
		return (byte) (-readByte());
	}

	public int readShort() {
		return bytes.readShort();
	}

	public int readShortA() {
		return ((bytes.readByte() & 0xFF) << 8) | (bytes.readByte() - 128 & 0xFF);
	}

	public int readLEShort() {
		return (bytes.readByte() & 0xFF) | ((bytes.readByte() & 0xFF) << 8);
	}

	public int readLEShortA() {
		return (bytes.readByte() - 128 & 0xFF) | ((bytes.readByte() & 0xFF) << 8);
	}

	public String readRS2String() {
		StringBuilder sb = new StringBuilder();
		int b;
		while (bytes.readableBytes() > 0 && (b = readByte()) != 0)
			sb.append((char) b);
		return sb.toString();
	}
}
