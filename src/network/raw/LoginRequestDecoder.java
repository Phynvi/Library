package network.raw;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

import network.Connection;
import network.ConnectionHolder;
import network.NetworkRepository;
import network.packet.decoding.IncomingPacketDecoder;

/**
 * The {@code LoginRequestDecoder} decoded any information from the client after
 * it has requested information on logging in. The {@code LoginRequestDecoder}
 * responds by sending information of the holder that is logging in.
 * 
 * @author Albert Beaupre
 */
public class LoginRequestDecoder extends ByteToMessageDecoder {

    /**
     * Decodes the login request made by the client. The information of the
     * holder logging in must be sent to the client in response.
     */
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
	if (in.readableBytes() >= 3) {
	    int state = in.readByte() & 0xFF;
	    int packetSize = in.readShort() & 0xFFFF;
	    if (packetSize != in.readableBytes())
		throw new IOException("Invalid login packet size");

	    int revision = in.readInt();

	    RawHandler handler = NetworkRepository.getRawHandler(revision);
	    if (handler == null)
		throw new UnsupportedOperationException("Revision not supported for login: " + revision);
	    if (!handler.loaded) {
		handler.loadRawHandler();
		handler.loaded = true;
	    }
	    Connection connection = new Connection(ctx.channel(), revision);
	    ConnectionHolder holder = handler.createConnectionHolder(connection, in, state);

	    ctx.pipeline().replace("decoder", "decoder", new IncomingPacketDecoder(handler, holder));
	}
    }
}
