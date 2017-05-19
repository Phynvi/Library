package network.event;

import io.netty.channel.ChannelHandlerContext;
import event.Event;

public class ChannelUnregisteredEvent extends Event {
    private final ChannelHandlerContext context;

    public ChannelUnregisteredEvent(ChannelHandlerContext context) {
	this.context = context;
    }

    public ChannelHandlerContext getContext() {
	return context;
    }
}
