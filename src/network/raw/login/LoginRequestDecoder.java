package network.raw.login;

import java.io.IOException;
import java.util.List;

import infrastructure.GlobalVariables;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import network.Connection;
import network.ConnectionHolder;
import network.NetworkRepository;
import network.cryptogrophy.ISAACCipher;
import network.packet.decoding.IncomingPacketDecoder;
import network.packet.encoding.OutgoingPacketEncoder;
import network.raw.RawHandler;

/**
 * The {@code LoginRequestDecoder} decoded any information from the client after it has requested
 * information on logging in. The {@code LoginRequestDecoder} responds by sending information of the
 * holder that is logging in.
 * 
 * @author Albert Beaupre
 */
public class LoginRequestDecoder extends ByteToMessageDecoder {

	/**
	 * Decodes the login request made by the client. The information of the holder logging in must be
	 * sent to the client in response.
	 */
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= 3) {
			int state = in.readUnsignedByte();
			int packetSize = in.readShort() & 0xFFFF;
			if (packetSize != in.readableBytes())
				throw new IOException("Invalid login packet size");

			int revision = in.readInt();
			while (in.readByte() != 10); //TODO find correct way to identify RSA

			int[] isaacKeysIn = new int[4], isaacKeysOut = new int[4];

			for (int i = 0; i < isaacKeysIn.length; i++) {
				isaacKeysIn[i] = in.readInt();
				isaacKeysOut[i] = isaacKeysIn[i] + 50;
			}
			ctx.channel().attr(GlobalVariables.ISAAC_KEYS_IN).set(isaacKeysIn);
			ctx.channel().attr(GlobalVariables.ISAAC_KEYS_OUT).set(isaacKeysOut);

			RawHandler handler = NetworkRepository.getRawHandler(revision);
			if (handler == null)
				throw new UnsupportedOperationException("Revision not supported for login: " + revision);
			Connection connection = new Connection(ctx.channel(), revision);
			ConnectionHolder holder = handler.createConnectionHolder(connection, in, state);

			ctx.pipeline().replace("decoder", "decoder", new IncomingPacketDecoder(handler, holder, new ISAACCipher(isaacKeysIn)));
			ctx.pipeline().replace("encoder", "encoder", new OutgoingPacketEncoder(new ISAACCipher(isaacKeysOut)));
		}
	}
}
