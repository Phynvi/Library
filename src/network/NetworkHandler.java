package network;

import java.util.logging.Logger;

import infrastructure.GlobalVariables;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import network.cryptogrophy.ISAACCipher;
import network.event.ChannelActiveEvent;
import network.event.ChannelInactiveEvent;
import network.event.ChannelUnregisteredEvent;
import network.packet.decoding.DecodedPacket;
import network.packet.decoding.PacketDecoder;
import network.raw.RawHandler;

/**
 * The {@code NetworkHandler} class handles all receiving, sending, unregistering, reading, and
 * activations of a {@code Channel} for the server. When a {@code Channel} becomes active, the
 * {@link network.event.ChannelActiveEvent} is called. When a {@code Channel} becomes inactive, the
 * {@link network.event.ChannelInactiveEvent} is called. When a {@code Channel} becomes
 * unregistered, the {@link network.event.ChannelUnregisteredEvent} is called.
 * 
 * @author Albert Beaupre
 */
public class NetworkHandler extends ChannelInboundHandlerAdapter {

	private final ConnectionHolder holder;
	private final RawHandler handler;
	private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

	/**
	 * Constructs a new {@code NetworkHandler} with a specified {@code ConnectionHolder} as the holder
	 * of the {@code Connection}.
	 * 
	 * @param handler
	 *            the {@code RawHandler} used to get to this decoding process
	 * 
	 * @param holder
	 *            the holder of the {@code Connection}.
	 * @param isaac
	 *            the isaac cipher.
	 */
	public NetworkHandler(RawHandler handler, ConnectionHolder holder, ISAACCipher inCipher) {
		this.handler = handler;
		this.holder = holder;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.
	 * channel.ChannelHandlerContext)
	 */
	public void channelActive(ChannelHandlerContext ctx) {
		new ChannelActiveEvent(ctx).call();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty
	 * .channel.ChannelHandlerContext)
	 */
	public void channelInactive(ChannelHandlerContext ctx) {
		new ChannelInactiveEvent(ctx).call();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.
	 * netty.channel.ChannelHandlerContext)
	 */
	public void channelUnregistered(ChannelHandlerContext ctx) {
		new ChannelUnregisteredEvent(ctx).call();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel
	 * .ChannelHandlerContext, java.lang.Object)
	 */
	public void channelRead(ChannelHandlerContext ctx, Object message) {
		if (message == null)
			return;
		if (message instanceof DecodedPacket) {
			DecodedPacket packet = (DecodedPacket) message;
			PacketDecoder<ConnectionHolder> processor = handler.getPacketDecoder(packet.getOpcode());
			if (processor != null) {
				processor.process(holder, packet);
			} else {
				if (GlobalVariables.isDebugEnabled())
					LOGGER.warning(String.format("Unprocessed Packet[opcode=%s, length=%s]", packet.getOpcode(), packet.getLength()));
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see io.netty.channel.ChannelInboundHandlerAdapter#channelReadComplete(io.
	 * netty.channel.ChannelHandlerContext)
	 */
	public void channelReadComplete(ChannelHandlerContext ctx) {}

}
