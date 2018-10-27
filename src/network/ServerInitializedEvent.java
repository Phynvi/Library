package network;

import event.Event;
import infrastructure.threads.ServerThread;

public class ServerInitializedEvent extends Event {

	private final ServerThread server;

	public ServerInitializedEvent(ServerThread server) {
		this.server = server;
	}

	public ServerThread getServer() {
		return server;
	}

}
