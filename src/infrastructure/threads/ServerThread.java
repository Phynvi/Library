package infrastructure.threads;

import infrastructure.Core;
import infrastructure.CoreThread;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.logging.Logger;
import network.NetworkHandler;
import network.packet.encoding.OutgoingPacketEncoder;
import network.raw.handshake.HandshakeDecoder;

/**
 * @author Albert Beaupre
 */
public final class ServerThread extends CoreThread {

	private final Logger logger = Logger.getLogger(getClass().getName());

	private ServerBootstrap bootstrap;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workerGroup;

	private final int port;
	private boolean running;

	/**
	 * Constructs a new {@code ServerThread} from the specified {@code port}.
	 * 
	 * @param port
	 *           the port of this server
	 */
	public ServerThread(int port) {
		super("Server Thread", Thread.NORM_PRIORITY, false);
		this.port = port;
		this.bootstrap = new ServerBootstrap();
		this.bossGroup = new NioEventLoopGroup();
		this.workerGroup = new NioEventLoopGroup();
	}

	/**
	 * Starts this {@code ServerThread} from the port previously assigned.
	 */
	public final void start() {
		if (running)
			return;
		Core.submitThread(this);
	}

	@Override
	public void run() {
		try {
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("encoder", new OutgoingPacketEncoder());
					ch.pipeline().addLast("decoder", new HandshakeDecoder());
					ch.pipeline().addLast("handler", new NetworkHandler());
				}
			}).option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true);
			ChannelFuture f = bootstrap.bind(port).sync();

			while (!f.isSuccess()) {
			} // wait until it is successfull

			running = f.isSuccess();

			logger.info("Initialized ServerThread on port " + port + ".");

			f.channel().closeFuture().sync();
		} catch (Exception e) {
			running = false;
			e.printStackTrace();
		} finally {
			running = false;
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}
}
