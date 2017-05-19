package network.raw;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

import network.Connection;
import network.NetworkRepository;
import network.packet.decoding.IncomingPacketDecoder;

/**
 * The {@code LoginRequestDecoder} decoded any information from the client after
 * it has requested information on logging in. The {@code LoginRequestDecoder}
 * responds by sending information of the player that is logging in.
 * 
 * @author Albert Beaupre
 */
public class LoginRequestDecoder extends ByteToMessageDecoder {

    /**
     * Decodes the login request made by the client. The information of the
     * player logging in must be sent to the client in response.
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
	    handler.loadRawHandler();
	    ctx.pipeline().replace("decoder", "decoder", new IncomingPacketDecoder(handler, handler.createConnectionHolder(new Connection(ctx.channel()), in, state)));
	}
    }
}
