package network.raw.login;

import java.io.IOException;
import java.util.List;

import infrastructure.GlobalVariables;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.AttributeKey;
import network.Connection;
import network.ConnectionHolder;
import network.NetworkHandler;
import network.NetworkRepository;
import network.cryptogrophy.ISAACCipher;
import network.packet.decoding.IncomingPacketDecoder;
import network.packet.encoding.OutgoingPacketEncoder;
import network.raw.RawHandler;

public class LoginRequestDecoder extends ByteToMessageDecoder {
	public static final AttributeKey<ConnectionHolder> CON_HOLD_KEY = AttributeKey.valueOf("connection_holder");

	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() >= 3) {
			int state = in.readByte() & 0xFF;
			int packetSize = in.readShort() & 0xFFFF;
			if (packetSize != in.readableBytes())
				throw new IOException("Invalid login packet size");

			int revision = in.readInt();

			while (in.readByte() != 10); // TODO RSA

			int[] isaacKeysIn = new int[4];
			int[] isaacKeysOut = new int[4];

			for (int i = 0; i < isaacKeysIn.length; ++i) {
				isaacKeysIn[i] = in.readInt();
				isaacKeysOut[i] = isaacKeysIn[i] + 50;
			}

			ctx.channel().attr(GlobalVariables.ISAAC_KEYS_IN).set(isaacKeysIn);
			ctx.channel().attr(GlobalVariables.ISAAC_KEYS_OUT).set(isaacKeysOut);
			ctx.pipeline().replace("encoder", "encoder", new OutgoingPacketEncoder(new ISAACCipher(isaacKeysOut)));
			RawHandler handler = NetworkRepository.getRawHandler(revision);
			if (handler == null)
				throw new UnsupportedOperationException("Revision not supported for login: " + revision);

			Connection connection = new Connection(ctx.channel(), revision);
			ConnectionHolder holder = handler.createConnectionHolder(connection, in, state);
			ctx.channel().attr(CON_HOLD_KEY).set(holder);
			ctx.pipeline().replace("handler", "handler", new NetworkHandler(handler, holder, new ISAACCipher(isaacKeysIn)));
			ctx.pipeline().replace("decoder", "decoder", new IncomingPacketDecoder(handler, new ISAACCipher(isaacKeysIn)));
		}

	}
}