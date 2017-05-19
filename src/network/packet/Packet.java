package network.packet;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * A {@code Packet} contains bytes of data which contains information for the
 * server or client to determine any actions based on the data.
 * 
 * @author Albert Beaupre
 *
 * @see network.packet.PacketType
 */
public abstract class Packet {

    protected final PacketType type;
    protected final ByteBuf bytes;
    protected final int opcode;

    /**
     * Constructs a new {@code Packet} with the bytes of data from the specified
     * {@code bytes} which is identified by the specified {@code opcode}.
     * 
     * @param type
     *            the type of packet
     * @param bytes
     *            the bytes of data for this packet
     * @param opcode
     *            the opcode of this packet
     */
    public Packet(PacketType type, ByteBuf bytes, int opcode) {
	this.type = type;
	this.bytes = bytes;
	this.opcode = opcode;
    }

    /**
     * Constructs a new {@code Packet} with the specified {@code opcode} and
     * {@code type}.
     * 
     * @param type
     *            the type of packet
     * @param opcode
     *            the opcode of this packet
     */
    public Packet(PacketType type, int opcode) {
	this(type, Unpooled.buffer(), opcode);
    }

    /**
     * Constructs a new {@code Packet} with the specified {@code opcode} that
     * has a default {@code PacketType} of {@value PacketType#STANDARD}
     * 
     * @param opcode
     *            the opcode of this packet
     */
    public Packet(int opcode) {
	this(PacketType.STANDARD, opcode);
    }

    /**
     * Constructs a new {@code Packet}, which is represented as a <b>raw
     * packet</b>, with a default opcode of -1.
     */
    public Packet() {
	this(-1);
    }

    /**
     * Returns the bytes of data in a {@code ByteBuf}.
     * 
     * @return the bytes of data
     */
    public ByteBuf getBytes() {
	return bytes;
    }

    /**
     * Returns the {@code PacketType} of this {@code Packet}.
     * 
     * @return the type
     */
    public PacketType getType() {
	return type;
    }

    /**
     * Returns the opcode of this {@code Packet}.
     * 
     * @return the opcode
     */
    public int getOpcode() {
	return opcode;
    }

    /**
     * Returns true if this {@code Packet} is represents as a 'raw'
     * packet--meaning without an opcode;
     * 
     * <p>
     * This method is effectively equivalent to:
     * 
     * <pre>
     * boolean isRaw = getOpcode() == -1;
     * </pre>
     * 
     * @return true if this packet is raw
     */
    public boolean isRaw() {
	return opcode == -1;
    }

    /**
     * Returns the length of readable bytes within this {@code Packet}.
     * 
     * @return the length of readable bytes
     */
    public int getLength() {
	return bytes.readableBytes();
    }
}
