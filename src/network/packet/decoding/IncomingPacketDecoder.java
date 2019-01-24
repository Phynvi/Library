package network.packet.decoding;

import java.util.List;

import infrastructure.GlobalVariables;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import network.cryptogrophy.ISAACCipher;
import network.packet.PacketType;
import network.raw.RawHandler;

/**
 * The {@code IncomingPacketDecoder} receives and in-game {@code Packet} and decodes the information
 * and turns it into a {@code DecodedPacket}.
 * 
 * @author Albert Beaupre
 */
public class IncomingPacketDecoder extends ByteToMessageDecoder {

	private final RawHandler handler;
	private final ISAACCipher inCipher;

	/**
	 * Constructs a new {@code IncomingPacketDecoder}
	 * 
	 * @param handler
	 *            the {@code RawHandler} used to get to this decoding process
	 * 
	 * @param isaac
	 *            the isaac cipher.
	 */
	public IncomingPacketDecoder(RawHandler handler, ISAACCipher inCipher) {
		this.handler = handler;
		this.inCipher = inCipher;
	}

	/**
	 * Decodes the bytes while a holder is in-game and turns the decoded bytes into a
	 * {@code DecodedPacket} for easier reading.
	 * 
	 * @param ctx
	 *            The context of the channel handler
	 * @param in
	 *            The incoming bytes
	 * @param out
	 *            The outgoing bytes
	 */
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.isReadable()) {
			int opcode = in.readByte() & 0xFF;
			if (GlobalVariables.isISAACEnabled())
				opcode -= inCipher.getNextValue() & 0xFF;
			if (opcode < 0) {
				in.discardReadBytes();
				return;
			}
			int length = handler.getPacketSizes()[opcode];
			if (length == -1 && in.isReadable())
				length = in.readByte() & 0xFF;
			if (length <= in.readableBytes()) {
				if (length < 1) {
					out.add(new DecodedPacket(PacketType.STANDARD, Unpooled.buffer(), opcode));
					return;
				}
				byte[] payload = new byte[length];
				in.readBytes(payload, 0, length);
				out.add(new DecodedPacket(PacketType.STANDARD, Unpooled.wrappedBuffer(payload), opcode));
			}
		}
	}
}
