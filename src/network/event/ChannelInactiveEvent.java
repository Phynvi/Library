package network.event;

import io.netty.channel.ChannelHandlerContext;
import event.Event;

public class ChannelInactiveEvent extends Event {

    private final ChannelHandlerContext context;

    public ChannelInactiveEvent(ChannelHandlerContext context) {
	this.context = context;
    }

    public ChannelHandlerContext getContext() {
	return context;
    }

}
