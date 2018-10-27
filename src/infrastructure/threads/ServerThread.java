package infrastructure.threads;

import java.util.logging.Logger;

import infrastructure.CoreThread;
import infrastructure.GlobalVariables;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import network.NetworkHandler;
import network.ServerInitializedEvent;
import network.World;
import network.packet.encoding.OutgoingPacketEncoder;
import network.raw.handshake.HandshakeDecoder;

/**
 * @author Albert Beaupre
 */
public class ServerThread extends CoreThread {

	private final Logger logger = Logger.getLogger(getClass().getName());

	private ServerBootstrap bootstrap;
	private NioEventLoopGroup bossGroup;
	private NioEventLoopGroup workerGroup;

	private final int port;

	private final World world;

	/**
	 * Constructs a new {@code ServerThread} from the specified {@code port}.
	 * 
	 * @param port
	 *            the port of this server
	 */
	public ServerThread(World world, int port) {
		super("Server Thread", Thread.NORM_PRIORITY, false);
		this.world = world;
		this.port = port;
		this.bootstrap = new ServerBootstrap();
		this.bossGroup = new NioEventLoopGroup();
		this.workerGroup = new NioEventLoopGroup();
	}

	@Override
	public void start() {
		super.start();

		new ServerInitializedEvent(this).call();
	}

	@Override
	public void run() {
		try {
			bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("encoder", new OutgoingPacketEncoder(null));
					ch.pipeline().addLast("decoder", new HandshakeDecoder());
					ch.pipeline().addLast("handler", new NetworkHandler());
					ch.attr(GlobalVariables.WORLD_KEY).set(world);
				}
			}).option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.TCP_NODELAY, true);
			ChannelFuture f = bootstrap.bind(port).sync();

			while (!f.isSuccess()) {} // wait until it is successfull

			logger.info("World Initialized: " + world.getName() + " on port " + port + ".");

			f.channel().closeFuture().sync();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public World getWorld() {
		return world;
	}
}