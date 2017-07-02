package network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import network.event.ChannelActiveEvent;
import network.event.ChannelInactiveEvent;
import network.event.ChannelUnregisteredEvent;

/**
 * The {@code NetworkHandler} class handles all receiving, sending,
 * unregistering, reading, and activations of a {@code Channel} for the server.
 * When a {@code Channel} becomes active, the
 * {@link network.event.ChannelActiveEvent} is called. When a {@code Channel}
 * becomes inactive, the {@link network.event.ChannelInactiveEvent} is called.
 * When a {@code Channel} becomes unregistered, the
 * {@link network.event.ChannelUnregisteredEvent} is called.
 * 
 * @author Albert Beaupre
 */
public class NetworkHandler extends ChannelInboundHandlerAdapter {

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.netty.channel.ChannelInboundHandlerAdapter#channelActive(io.netty.
     * channel.ChannelHandlerContext)
     */
    public void channelActive(ChannelHandlerContext ctx) {
	new ChannelActiveEvent(ctx).call();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.netty.channel.ChannelInboundHandlerAdapter#channelInactive(io.netty
     * .channel.ChannelHandlerContext)
     */
    public void channelInactive(ChannelHandlerContext ctx) {
	new ChannelInactiveEvent(ctx).call();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.netty.channel.ChannelInboundHandlerAdapter#channelUnregistered(io.
     * netty.channel.ChannelHandlerContext)
     */
    public void channelUnregistered(ChannelHandlerContext ctx) {
	new ChannelUnregisteredEvent(ctx).call();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.netty.channel.ChannelInboundHandlerAdapter#channelRead(io.netty.channel
     * .ChannelHandlerContext, java.lang.Object)
     */
    public void channelRead(ChannelHandlerContext ctx, Object message) {
	if (message == null)
	    return;
	if (message instanceof ByteBuf)
	    ctx.writeAndFlush(((ByteBuf) message));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * io.netty.channel.ChannelInboundHandlerAdapter#channelReadComplete(io.
     * netty.channel.ChannelHandlerContext)
     */
    public void channelReadComplete(ChannelHandlerContext ctx) {}

}
