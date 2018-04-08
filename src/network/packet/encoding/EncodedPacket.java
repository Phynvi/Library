package network.packet.encoding;

import io.netty.buffer.ByteBuf;
import network.packet.Packet;
import network.packet.PacketType;

/**
 * byte = byte (1 byte) word = short (2 bytes) dWord = double word = integer (2 * 2 bytes = 4 bytes)
 * qWord = quadruple word = double dword = long (2 * 4 bytes = 8 bytes)
 * 
 * BigEndian is the normal way of writing a sequence of bytes, where the bytes with the higher
 * (possible) values come first (big end first). LittleEndian is another way of writing a sequence
 * of bytes, where the bytes with the lower (possible) values come first (little end first).
 * Example: an integer represented by 4 bytes ABCD. BigEndian: ABCD (A is relatively worth more than
 * B, ...). LittleEndian: DCBA (D is relatively worth less than C, ...).
 * 
 * CreateFrame marks the start of creating a packet with a known size.
 * 
 * EndFrameVarSize marks the end of a packet with 'unknown' size where the number of bytes is
 * smaller than or equal to 0xFF (255). EndFrameVarSizeWord marks the end of a packet with 'unknown'
 * size where the number of bytes is smaller than or equal to 0xFFFF (65535).
 * 
 * Write String writes a string ending with either 0 (closing zero) or 10 (line feed) (I'm not sure
 * which one).
 * 
 * Knowing this you can combine the words to get a meaningful explanation: Example
 * WriteLittleEndianWord: A short represented by the bytes AB: BA.
 * 
 * If in the method names Big or Little Endian isn't specified, it's Big endian.
 * 
 * A short, represented by the bytes: AB. WriteLEWordA: (B + 128) A WriteWordA (WriteBEWordA): A (B
 * + 128) As you can see the order (LE/BE) still stands. The 'A' means that during the writing, 128
 * should be added to the lowest byte (here being B).
 * 
 * If the names don't help you out with 'converting'. Then look at what it does (You may want to
 * learn about bitwise operators).
 */

/************************************************/
/* ABOVE IS AN EXPLANATION */
/**********************************************/

/**
 * This type of {@code Packet} is used for encoding data so the client can read it.
 * 
 * @author Albert Beaupre
 * 
 * @see network.packet.Packet
 */
public class EncodedPacket extends Packet {

	private static final int[] BIT_MASK_OUT = new int[32];

	static {
		for (int i = 0; i < BIT_MASK_OUT.length; i++)
			BIT_MASK_OUT[i] = (1 << i) - 1;
	}

	private int bitPosition;

	/**
	 * Constructs a new {@code EncodedPacket} with the specified {@code opcode} and {@code type}.
	 * 
	 * @param type
	 *            the type of packet
	 * @param opcode
	 *            the opcode of this packet
	 */
	public EncodedPacket(PacketType type, int opcode) {
		super(type, opcode);
	}

	/**
	 * Constructs a new {@code EncodedPacket} with the specified {@code opcode} with a default type of
	 * {@link PacketType#STANDARD}.
	 * 
	 * @param opcode
	 *            the opcode of this packet
	 */
	public EncodedPacket(int opcode) {
		super(opcode);
	}

	/**
	 * Constructs a new {@code EncodedPacket}, which is represented as a <b>raw packet</b>, with a
	 * default opcode of -1 and default type of {@link PacketType#STANDARD}.
	 */
	public EncodedPacket() {
		super();
	}

	/**
	 * Writes the bytes of the specified {@code packet} to this {@code EncodedPacket}.
	 * 
	 * @param packet
	 *            the packet to write from
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket write(EncodedPacket packet) {
		return write(packet.getBytes());
	}

	/**
	 * Writes the bytes of the specified {@code buffer} to this {@code EncodedPacket}.
	 * 
	 * @param buffer
	 *            the buffer to write from
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket write(ByteBuf buffer) {
		this.bytes.writeBytes(buffer);
		return this;
	}

	public EncodedPacket writeBytes(byte[] data, int offset, int length) {
		this.bytes.writeBytes(data, offset, length);
		return this;
	}

	public EncodedPacket writeByteS(int val) {
		this.bytes.writeByte((byte) (128 - val));
		return this;
	}

	public EncodedPacket writeByteA(int val) {
		this.bytes.writeByte((byte) (val + 128));
		return this;
	}

	public EncodedPacket writeBytesA(byte[] data, int offset, int len) {
		for (int k = offset; k < len; k++)
			this.bytes.writeByte((byte) (data[k] + 128));
		return this;
	}

	public EncodedPacket writeByteC(int val) {
		this.bytes.writeByte((byte) (-val));
		return this;
	}

	public EncodedPacket writeTriByte(int val) {
		return writeBytes((byte) (val >> 16), (byte) (val >> 8), (byte) val);
	}

	/**
	 * Writes a byte to this {@code EncodedPacket}.
	 * 
	 * @param b
	 *            the byte value to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeByte(int b) {
		this.bytes.writeByte(b);
		return this;
	}

	/**
	 * Writes the specified values of {@code b} as bytes to this {@code EncodedPacket}.
	 * 
	 * @param b
	 *            the values to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeBytes(int... b) {
		for (int by : b)
			this.bytes.writeByte(by);
		return this;
	}

	public EncodedPacket writeBytes(byte[] bytes) {
		this.bytes.writeBytes(bytes);
		return this;
	}

	/**
	 * Writes the specified values of {@code s} as shorts to this {@code EncodedPacket}.
	 * 
	 * @param s
	 *            the values to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeShort(int... s) {
		for (int sh : s)
			this.bytes.writeShort(sh);
		return this;
	}

	/**
	 * Writes a Little-Endian word short based on the specified argument.
	 * 
	 * <p>
	 * This method is effectively equivalent to:
	 * 
	 * <pre>
	 * writeByte((byte) value);
	 * writeByte((byte) (value &gt;&gt; 8));
	 * </pre>
	 * 
	 * @param value
	 *            the short value to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeLEShort(int value) {
		return writeBytes((byte) value, (byte) (value >> 8));
	}

	/**
	 * Writes a word short based on the specified argument.
	 * 
	 * <p>
	 * This method is effectively equivalent to:
	 * 
	 * <pre>
	 * writeByte((byte) (value &gt;&gt; 8));
	 * writeByte((byte) (value + 128));
	 * </pre>
	 * 
	 * @param value
	 *            the short value to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeShortA(int value) {
		writeByte((byte) (value >> 8));
		writeByte((byte) (value + 128));
		return this;
	}

	/**
	 * Writes a Little-Endian word short based on the specified argument.
	 * 
	 * <p>
	 * This method is effectively equivalent to:
	 * 
	 * <pre>
	 * writeByte((byte) (value + 128));
	 * writeByte((byte) (value &gt;&gt; 8));
	 * </pre>
	 * 
	 * @param value
	 *            the short value to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeLEShortA(int value) {
		bytes.writeByte((byte) (value + 128));
		bytes.writeByte((byte) (value >> 8));
		return this;
	}

	/**
	 * Writes the specified values of {@code i} as integers to this {@code EncodedPacket}.
	 * 
	 * @param i
	 *            the values to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeInt(int... i) {
		for (int in : i)
			this.bytes.writeInt(in);
		return this;
	}

	/**
	 * Writes a Litten-Endian word integer based on the specified argument.
	 * 
	 * @param val
	 *            the value to write as a litten-endian word
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeLEInt(int val) {
		writeBytes((byte) (val), (byte) (val >> 8), (byte) (val >> 16), (byte) (val >> 24));
		return this;
	}

	public EncodedPacket writeInt1(int val) {
		return writeBytes((byte) (val >> 8), (byte) val, (byte) (val >> 24), (byte) (val >> 16));
	}

	public EncodedPacket writeInt2(int val) {
		return writeBytes((byte) (val >> 16), (byte) (val >> 24), (byte) val, (byte) (val >> 8));
	}

	public EncodedPacket writeIntSmart(int val) {
		if (val >= 32768) {
			writeInt(val + 32768);
		} else {
			writeShort(val);
		}
		return this;
	}

	/**
	 * Writes the specified values of {@code l} as longs to this {@code EncodedPacket}.
	 * 
	 * @param l
	 *            the values to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeLong(long... l) {
		for (long lo : l)
			this.bytes.writeLong(lo);
		return this;
	}

	/**
	 * Writes the specified values of {@code m} as mediums to this {@code EncodedPacket}.
	 * 
	 * @param m
	 *            the values to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeMedium(int... m) {
		for (int me : m)
			this.bytes.writeMedium(me);
		return this;
	}

	/**
	 * Writes the bytes of the specified {@code string} with a trailing 0 byte value to this
	 * {@code EncodedPacket}.
	 * 
	 * @param string
	 *            the string to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeRS2String(String string) {
		return writeBytes(string.getBytes()).writeBytes((byte) 0);
	}

	/**
	 * Writes the bytes of the specified {@code string} with a leading and trailing 0 byte value to this
	 * {@code EncodedPacket}.
	 * 
	 * @param string
	 *            the string to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeGJString(String string) {
		return writeBytes((byte) 0).writeBytes(string.getBytes()).writeBytes((byte) 0);
	}

	/**
	 * Writes a {@code String} with a trailing byte value of 0 that cannot have any {@code null} value
	 * within the specified {@codes string}.
	 * 
	 * @param string
	 *            the string to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writePJStr1(String string) {
		if (string.indexOf((char) 0) >= 0)
			throw new IllegalArgumentException("The given string may not contain a NULL (byte 0) character");
		return writeBytes(string.getBytes()).writeByte((byte) 0);
	}

	/**
	 * Writes the value as a byte if the value is >= 128 otherwise it will write it as a short + 32768.
	 * 
	 * @param b
	 *            the values to write
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeSmart(int s) {
		return s >= 128 ? writeShort(s + 32768) : writeBytes((byte) s);
	}

	/**
	 * Sets the bit position within this {@code EncodedPacket} to start writing bit information.
	 * 
	 * <p>
	 * The value of the bit position is equivalent to:
	 * 
	 * <pre>
	 * bitPosition = writerIndex * 8;
	 * </pre>
	 * 
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket startBitAccess() {
		bitPosition = bytes.writerIndex() * 8;
		return this;
	}

	/**
	 * Sets the writer index back to the value it was before bit information had started being written.
	 * 
	 * <p>
	 * The value of the writer index is equivalent to:
	 * 
	 * <pre>
	 * writerIndex = (bitPosition + 7) / 8;
	 * </pre>
	 * 
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket finishBitAccess() {
		bytes.writerIndex((bitPosition + 7) / 8);
		return this;
	}

	/**
	 * Writes bits based on the specified arguments.
	 * 
	 * @param bitCount
	 *            The number of bits which need to be written
	 * @param value
	 *            The value.
	 * @return the instance of this {@code EncodedPacket} for chaining
	 */
	public EncodedPacket writeBits(int bitCount, int value) {
		int bytePos = bitPosition >> 3;
		int bitOffset = 8 - (bitPosition & 7);
		bitPosition += bitCount;
		int pos = (bitPosition + 7) / 8;
		bytes.ensureWritable(pos + 1);
		bytes.writerIndex(pos);
		byte b;
		for (; bitCount > bitOffset; bitOffset = 8) {
			b = bytes.getByte(bytePos);
			bytes.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			bytes.setByte(bytePos, (byte) (b | (value >> (bitCount - bitOffset)) & BIT_MASK_OUT[bitOffset]));
			bytePos++;
			bitCount -= bitOffset;
		}
		b = bytes.getByte(bytePos);
		if (bitCount == bitOffset) {
			bytes.setByte(bytePos, (byte) (b & ~BIT_MASK_OUT[bitOffset]));
			bytes.setByte(bytePos, (byte) (b | value & BIT_MASK_OUT[bitOffset]));
		} else {
			bytes.setByte(bytePos, (byte) (b & ~(BIT_MASK_OUT[bitCount] << (bitOffset - bitCount))));
			bytes.setByte(bytePos, (byte) (b | (value & BIT_MASK_OUT[bitCount]) << (bitOffset - bitCount)));
		}
		return this;
	}
}
