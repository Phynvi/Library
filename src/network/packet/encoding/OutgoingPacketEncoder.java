package network.packet.encoding;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import network.cryptogrophy.ISAACCipher;
import network.packet.PacketType;

/**
 * The {@code OutgoingPacketEncoder} takes any {@code EncodedPacket} being sent through the network
 * from the server to the client and re-writes the information for the client to read plainly.
 * 
 * @author Albert Beaupre
 */
public class OutgoingPacketEncoder extends MessageToByteEncoder<EncodedPacket> {

	private final ISAACCipher outCipher;

	public OutgoingPacketEncoder(ISAACCipher outCipher) {
		this.outCipher = outCipher;
	}

	/**
	 * Encodes any {@code EncodedPacket} being sent through the network from the server to the client
	 * and re-writes the information for the client to read plainly.
	 */
	protected void encode(ChannelHandlerContext ctx, EncodedPacket out, ByteBuf in) throws Exception {
		if (!out.isRaw()) {
			int packetBufferLength = out.getLength();
			int bufferLength = 1 + packetBufferLength + out.getType().getSize();
			ByteBuf response = Unpooled.buffer(bufferLength);
			if (out.getOpcode() > 127)
				response.writeByte(128);//TODO this might break but you wont know until u use a packet with this.
			response.writeByte(out.getOpcode() + outCipher.getNextValue() & 0xFF);
			if (out.getType() == PacketType.VAR_BYTE) {
				if (packetBufferLength > 255)
					return;
				response.writeByte(packetBufferLength);
			} else if (out.getType() == PacketType.VAR_SHORT) {
				if (packetBufferLength > 65535)
					return;
				response.writeShort(packetBufferLength);
			}
			response.writeBytes(out.getBytes());
			ctx.writeAndFlush(response);
			return;
		}
		ctx.writeAndFlush(out.getBytes());
	}
}
