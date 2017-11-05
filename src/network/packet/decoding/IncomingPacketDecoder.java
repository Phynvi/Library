package network.packet.decoding;

import infrastructure.Core;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import network.ConnectionHolder;
import network.packet.PacketType;
import network.raw.RawHandler;

/**
 * The {@code IncomingPacketDecoder} receives and in-game {@code Packet} and decodes the information
 * and turns it into a {@code DecodedPacket}.
 * 
 * @author Albert Beaupre
 */
public class IncomingPacketDecoder extends ByteToMessageDecoder {

	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
	private static final HashMap<Integer, int[]> PACKET_SIZES = new HashMap<>();

	static {
		PACKET_SIZES.put(637, new int[] { 8, -1, -1, 16, 6, 2, 8, 6, 3, -1, 16, 15, 0, 8, 11, 8, -1, -1, 3, 2, -3, -1, 7, 2, -1, 7, -1, 3, 3, 6, 4, 3, 0, 3, 4, 5, -1, -1, 7, 8, 4, -1, 4, 7, 3, 15, 8, 3, 2, 4, 18, -1, 1, 3, 7, 7, 4, -1, 8, 2, 7, -1, 1, -1, 3, 2, -1, 8, 3, 2, 3, 7, 3, 8, -1, 0, 7, -1, 11, -1, 3, 7, 8, 12, 4, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3, -3 });
		PACKET_SIZES.put(666, new int[] { 0, 7, -1, 8, 3, -1, 15, 8, 6, -1, 3, 8, -1, -1, 3, 4, 7, 8, 1, -1, 4, 2, -1, 7, 7, 8, 16, 3, 7, 3, -1, -1, 4, 0, 6, 5, 6, 4, 7, 7, 8, 0, 15, 3, 3, 7, -1, 3, 8, 7, -1, 3, 4, 18, 8, -1, 5, 11, 7, -1, 1, 3, -1, 4, 0, 11, 8, 2, -1, 3, 3, 16, 3, 2, -1, 7, 4, 2, 3, -1, -1, -1, -1, 3, 8, 8, 7, 0, -1, -1, 3, 3, 4, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });
	}

	private final ConnectionHolder holder;
	private final RawHandler handler;

	/**
	 * Constructs a new {@code IncomingPacketDecoder} with a specified {@code ConnectionHolder} as
	 * the holder of the {@code Connection}.
	 * 
	 * @param handler
	 *           the {@code RawHandler} used to get to this decoding process
	 * 
	 * @param holder
	 *           the holder of the {@code Connection}.
	 */
	public IncomingPacketDecoder(RawHandler handler, ConnectionHolder holder) {
		this.handler = handler;
		this.holder = holder;
	}

	/**
	 * Decodes the bytes while a holder is in-game and turns the decoded bytes into a
	 * {@code DecodedPacket} for easier reading.
	 * 
	 * @param ctx
	 *           The context of the channel handler
	 * @param in
	 *           The incoming bytes
	 * @param out
	 *           The outgoing bytes
	 */
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.isReadable()) {
			int opcode = in.readByte() & 0xFF;
			if (opcode < 0) {
				in.discardReadBytes();
				return;
			}
			int length = PACKET_SIZES.get(handler.getRevision())[opcode];
			if (length == -1 && in.isReadable())
				length = in.readByte() & 0xFF;
			if (length <= in.readableBytes()) {
				if (length < 1) {
					DecodedPacket packet = new DecodedPacket(PacketType.STANDARD, Unpooled.buffer(), opcode);
					PacketDecoder<ConnectionHolder> processor = handler.getPacketDecoder(opcode);
					if (processor != null) {
						processor.process(holder, packet);
					} else {
						if (Core.debugging)
							LOGGER.warning(String.format("Unprocessed Packet[opcode=%s, length=%s]", opcode, length));
					}
					return;
				}
				byte[] payload = new byte[length];
				in.readBytes(payload, 0, length);
				DecodedPacket packet = new DecodedPacket(PacketType.STANDARD, Unpooled.wrappedBuffer(payload), opcode);

				PacketDecoder<ConnectionHolder> processor = handler.getPacketDecoder(opcode);
				if (processor != null) {
					processor.process(holder, packet);
				} else {
					if (Core.debugging)
						LOGGER.warning(String.format("Unprocessed Packet[opcode=%s, length=%s]", opcode, length));
				}
			} else {
			}
		}
	}
}
