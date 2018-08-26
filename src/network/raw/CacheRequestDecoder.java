package network.raw;

import infrastructure.Core;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * The {@code CacheRequestDecoder} will decode any information from the client when it requests
 * information from the cache. The {@code CacheRequestDecoder} will respond by sending the
 * information of the cache.
 * 
 * @author Albert Beaupre
 */
public class CacheRequestDecoder extends ByteToMessageDecoder {

	private final RawHandler handler;

	/**
	 * Constructs a new {@code CacheRequestDecoder} with the specified {@code handler}, which will
	 * handle the cache response to the client.
	 * 
	 * @param handler
	 *            the handler that will handle the cacher response
	 */
	public CacheRequestDecoder(RawHandler handler) {
		this.handler = handler;
	}

	/**
	 * Decodes the cache request that the client has sent the server. The information from the cache
	 * must be sent back.
	 * 
	 * @param ctx
	 *            the context decoded
	 * @param in
	 *            the bytes incoming
	 * @param out
	 *            the bytes outgoing
	 */
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		while (in.readableBytes() >= 4) {
			final int opcode = in.readByte() & 0xFF;
			final int idx = in.readByte() & 0xFF;
			final int file = Core.getCache().getRevision() <= 666 ? (in.readShort() & 0xFFFF) : in.readInt() & 0xFFFF;

			switch (opcode) {
			case 1:
			case 0:
				Core.submitRegularTask(() -> ctx.writeAndFlush(handler.createCacheResponse(idx, file, opcode)));
				break;
			case 2: // The client is connected
				break;
			case 3: // The client is logged out
				// TODO create a type of log out event alongside the
				// ChannelUnregistered/ChannelInactive event
				break;
			case 4: // A new encryption byte is being used
				break;
			case 6: // Connection is being initiated
				break;
			case 7: // Connection should be closed
				// ctx.close();
				break;
			}
		}
	}
}
