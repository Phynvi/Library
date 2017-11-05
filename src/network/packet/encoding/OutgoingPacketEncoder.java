package network.packet.encoding;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import network.packet.PacketType;

/**
 * The {@code OutgoingPacketEncoder} takes any {@code EncodedPacket} being sent through the network
 * from the server to the client and re-writes the information for the client to read plainly.
 * 
 * @author Albert Beaupre
 */
public class OutgoingPacketEncoder extends MessageToByteEncoder<EncodedPacket> {

	/**
	 * Encodes any {@code EncodedPacket} being sent through the network from the server to the client
	 * and re-writes the information for the client to read plainly.
	 */
	protected void encode(ChannelHandlerContext ctx, EncodedPacket out, ByteBuf in) throws Exception {
		if (!out.isRaw()) {
			int packetLength = 1 + out.getLength() + out.getType().getSize();
			ByteBuf response = Unpooled.buffer(packetLength);
			if (out.getOpcode() > 127)
				response.writeByte(128);
			response.writeByte(out.getOpcode());
			if (out.getType() == PacketType.VAR_BYTE) {
				response.writeByte(out.getLength());
			} else if (out.getType() == PacketType.VAR_SHORT) {
				response.writeShort(out.getLength());
			}
			response.writeBytes(out.getBytes());
			ctx.writeAndFlush(response);
			return;
		}
		ctx.writeAndFlush(out.getBytes());
	}
}
