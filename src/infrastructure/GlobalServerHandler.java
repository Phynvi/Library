package infrastructure;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import infrastructure.threads.ServerThread;

/**
 * 
 * @author Albert Beaupre
 */
public final class GlobalServerHandler {

	/**
	 * 
	 */
	private GlobalServerHandler() {

	}

	private static final SortedSet<ServerThread> SERVERS = new TreeSet<>(new Comparator<ServerThread>() {
		@Override
		public int compare(ServerThread first, ServerThread second) {
			return Integer.compare(first.getWorld().getId(), second.getWorld().getId());
		}
	});

	/**
	 * 
	 * @return
	 */
	public static Map<Integer, World> getWorlds() {
		HashMap<Integer, World> worlds = new HashMap<>();
		for (ServerThread server : SERVERS)
			worlds.put(server.getWorld().getId(), server.getWorld());
		return Collections.unmodifiableMap(worlds);
	}

	/**
	 * 
	 * @param world
	 * @param port
	 */
	public static void addServer(World world, int port) {
		for (ServerThread server : SERVERS) {
			if (server.getWorld().getId() == world.getId())
				throw new IllegalArgumentException("There is already a world with id " + world.getId());
		}
		ServerThread server = new ServerThread(world, port);
		SERVERS.add(server);
	}

	/**
	 * 
	 */
	public static void startServers() {
		for (ServerThread server : SERVERS)
			server.start();
	}

}
