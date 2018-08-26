package network.raw.handshake;

import java.util.List;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import network.NetworkRepository;
import network.raw.CacheRequestDecoder;
import network.raw.RawHandler;
import network.raw.login.LoginRequestDecoder;

/**
 * The {@code HandshakeDecoder} will receive any incoming data from a client with the same port and
 * decode the first requests made by the client. The {@code HandshakeDecoder} will reply differently
 * based on the request of the RuneScape Client.
 * 
 * <p>
 * To see the different types of requests, view {@link network.raw.handshake.HandshakeRequest}
 * 
 * @author Albert Beaupre
 */
public class HandshakeDecoder extends ByteToMessageDecoder {

	/**
	 * Decodes the 'handshake' process between the client and this server from the specified incoming
	 * {@code ByteBuf}.
	 * 
	 * @param ctx
	 *            the context of the channel handler
	 * @param in
	 *            the bytes incoming
	 * @param out
	 *            the bytes outgoing
	 */
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		int requestId = in.readByte() & 0xff;

		HandshakeRequest request = HandshakeRequest.forId(requestId);
		if (request == null) {
			System.err.println("Unknown Handshake Request Id: " + requestId);
			return;
		}
		switch (request) {
		case UPDATE:
			int revision = in.readInt();

			RawHandler handler = NetworkRepository.getRawHandler(revision);
			if (handler == null)
				throw new NullPointerException("Unsupported revision on handshake: " + revision);
			int[] data = handler.getKeys();
			ByteBuf buffer = Unpooled.buffer(data.length + 1);
			buffer.writeByte(0);
			for (int a : data)
				buffer.writeInt(a);
			ctx.writeAndFlush(buffer);
			ctx.pipeline().replace("decoder", "decoder", new CacheRequestDecoder(handler));
			break;
		case LOGIN:
			ctx.writeAndFlush(Unpooled.wrappedBuffer(new byte[] { 0 }));
			ctx.pipeline().replace("decoder", "decoder", new LoginRequestDecoder());
			break;
		}
	}
}
