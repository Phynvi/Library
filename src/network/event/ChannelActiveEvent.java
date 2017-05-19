package network.event;

import io.netty.channel.ChannelHandlerContext;
import event.Event;

public class ChannelActiveEvent extends Event {

    private final ChannelHandlerContext context;

    public ChannelActiveEvent(ChannelHandlerContext context) {
	this.context = context;
    }

    public ChannelHandlerContext getContext() {
	return context;
    }
}
